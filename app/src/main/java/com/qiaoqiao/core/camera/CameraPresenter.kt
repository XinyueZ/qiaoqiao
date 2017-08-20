package com.qiaoqiao.core.camera

import android.content.Context
import android.net.Uri
import android.support.v4.app.FragmentActivity
import com.google.api.services.vision.v1.model.BatchAnnotateImagesResponse
import com.google.api.services.vision.v1.model.Status
import com.qiaoqiao.core.camera.crop.model.CropSource
import com.qiaoqiao.core.camera.vision.VisionPresenter
import com.qiaoqiao.repository.DsLoadedCallback
import com.qiaoqiao.repository.DsRepository
import org.apache.commons.io.FileUtils
import java.io.File
import java.io.IOException
import javax.inject.Inject

class CameraPresenter
@Inject constructor(private val view: CameraContract.View, private val dsRepository: DsRepository) : CameraContract.Presenter {
    private lateinit var visionPresenter: VisionPresenter

    @Inject
    fun onInjected() {
        view.setPresenter(this)
    }

    fun setVisionPresenter(visionPresenter: VisionPresenter) {
        this.visionPresenter = visionPresenter
        visionPresenter.cameraPresenter = this
    }

    override fun updateWhenResponse() {
        view.updateViewWhenResponse()
    }

    @Throws(IOException::class)
    override fun capturedByteArray(cxt: Context, bytes: ByteArray) {
        val file = File.createTempFile("captured", "jpeg", cxt.getCacheDir())
        FileUtils.writeByteArrayToFile(file, bytes)
        view.openCrop(CropSource(Uri.fromFile(file)))
    }

    override fun findAnnotateImages(bytes: ByteArray) {
        view.updateViewWhenRequest()
        dsRepository.onBytes(bytes, object : DsLoadedCallback() {
            override fun onVisionResponse(response: BatchAnnotateImagesResponse?) {
                super.onVisionResponse(response)
                if (response != null)
                    visionPresenter.addResponseToScreen(response, true)
            }

            override fun onVisionApiError(status: Status) {
                super.onVisionApiError(status)
                updateWhenResponse()
            }

            override fun onException(e: Exception) {
                super.onException(e)
                view.showError(e.toString())
                updateWhenResponse()
            }
        })
    }

    override fun begin(hostActivity: FragmentActivity) {}

    override fun end(hostActivity: FragmentActivity) {}
}