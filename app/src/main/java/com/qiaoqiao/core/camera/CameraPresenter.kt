package com.qiaoqiao.core.camera

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Rect
import android.graphics.YuvImage
import android.hardware.Camera
import android.net.Uri
import android.support.v4.app.FragmentActivity
import com.google.api.services.vision.v1.model.BatchAnnotateImagesResponse
import com.google.api.services.vision.v1.model.Status
import com.qiaoqiao.core.camera.crop.model.CropSource
import com.qiaoqiao.core.camera.tf.ImageClassifier
import com.qiaoqiao.core.camera.vision.VisionPresenter
import com.qiaoqiao.core.camera.vision.model.VisionEntity
import com.qiaoqiao.repository.DsLoadedCallback
import com.qiaoqiao.repository.DsRepository
import com.qiaoqiao.rx.IoToMainScheduleSingle
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import org.apache.commons.io.FileUtils
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import javax.inject.Inject

class CameraPresenter
@Inject constructor(
    private val view: CameraContract.View,
    private val imageClassifier: ImageClassifier,
    private val dsRepository: DsRepository
) : CameraContract.Presenter {
    private lateinit var visionPresenter: VisionPresenter
    private val compositeDisposable = CompositeDisposable()

    private fun addToAutoDispose(vararg disposables: Disposable) {
        compositeDisposable.addAll(*disposables)
    }

    private fun autoDispose() {
        compositeDisposable.clear()
    }

    @Inject
    fun onInjected() {
        view.setPresenter(this)
    }

    fun setVisionPresenter(visionPresenter: VisionPresenter) {
        this.visionPresenter = visionPresenter
        visionPresenter.cameraPresenter = this
    }

    override fun updateWhenResponse(visionEntity: VisionEntity) {
        view.updateViewWhenResponse(visionEntity)
    }

    @Throws(IOException::class)
    override fun capturedByteArray(cxt: Context, bytes: ByteArray) {
        val file = File.createTempFile("captured", "jpeg", cxt.getCacheDir())
        FileUtils.writeByteArrayToFile(file, bytes)
        view.openCrop(CropSource(Uri.fromFile(file)))
    }

    @SuppressLint("CheckResult")
    override fun onUpdated(data: ByteArray, previewSize: Camera.Size, previewFormat: Int) {
        addToAutoDispose(Single.create<String>({ emitter ->
            synchronized(this@CameraPresenter) {
                ByteArrayOutputStream().use {
                    YuvImage(
                        data,
                        previewFormat,
                        previewSize.width,
                        previewSize.height,
                        null
                    ).compressToJpeg(
                        Rect(
                            0,
                            0,
                            224,
                            224
                        ), 100, it
                    )
                    with(it.toByteArray()) {
                        BitmapFactory.decodeByteArray(this, 0, size)?.apply {
                            emitter.onSuccess(imageClassifier.classifyFrame(this))
                            if (!isRecycled) {
                                recycle()
                            }
                        }
                    }
                }
            }
        }).compose(IoToMainScheduleSingle()).subscribe({
            view.tfOutput(it)
        }, {}))
    }

    override fun findAnnotateImages(bytes: ByteArray) {
        view.updateViewWhenRequest()
        addToAutoDispose(dsRepository.onBytes(bytes, object : DsLoadedCallback() {
            override fun onVisionResponse(response: BatchAnnotateImagesResponse?) {
                super.onVisionResponse(response)
                if (response != null)
                    visionPresenter.collectVisions(response, true)
            }

            override fun onVisionApiError(status: Status) {
                super.onVisionApiError(status)
//                updateWhenResponse()
            }

            override fun onException(e: Throwable) {
                super.onException(e)
                view.showError(e.toString())
//                updateWhenResponse()
            }
        }))
    }

    override fun begin(hostActivity: FragmentActivity) {}

    override fun end(hostActivity: FragmentActivity) {
        autoDispose()
    }
}