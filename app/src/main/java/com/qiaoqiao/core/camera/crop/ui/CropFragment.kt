package com.qiaoqiao.core.camera.crop.ui

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Animatable
import android.net.Uri
import android.os.Bundle
import android.os.Vibrator
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.animation.GlideAnimation
import com.bumptech.glide.request.target.SimpleTarget
import com.qiaoqiao.app.PrefsKeys
import com.qiaoqiao.core.camera.crop.CropContract
import com.qiaoqiao.core.camera.crop.model.CropSource
import com.qiaoqiao.databinding.FragmentCropBinding
import com.qiaoqiao.utils.LL
import com.theartofdev.edmodo.cropper.CropImageView
import org.apache.commons.io.output.ByteArrayOutputStream



class CropFragment : Fragment(), CropContract.View,
        View.OnClickListener,
        CropImageView.OnCropImageCompleteListener {
    private var binding: FragmentCropBinding? = null
    private var presenter: CropContract.Presenter? = null
    private var uri = Uri.EMPTY
    private val vibrator: Vibrator by lazy {
        context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    }

    companion object {
        fun newInstance(cxt: Context): CropFragment = instantiate(cxt, CropFragment::class.java.name) as CropFragment
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentCropBinding.inflate(inflater, container, false)
        binding?.let {
            it.cropFb.setOnClickListener(this@CropFragment)
            it.cropIv.setOnCropImageCompleteListener(this@CropFragment)
        }
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        retainInstance = true
        showImage()
    }

    override fun setPresenter(presenter: CropContract.Presenter) {
        this.presenter = presenter
    }

    override fun getBinding() = binding

    override fun setCropSource(cropSource: CropSource) {
        uri = cropSource.uri
    }

    private fun showImage() {
        when (uri.toString().startsWith("http")) {
            true -> {
                LL.d(uri.toString())
                Glide.with(this)
                        .load("https://www.simponi.com/sites/www.simponi.com/files/logo.png").asBitmap()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .skipMemoryCache(false)
                        .into(object : SimpleTarget<Bitmap>() {
                            override fun onResourceReady(resource: Bitmap?, glideAnimation: GlideAnimation<in Bitmap>?) {
                                LL.d("onResourceReady")
                                when (resource) {
                                    null -> return
                                    else -> binding?.cropIv?.setImageBitmap(resource)
                                }
                            }
                        })
            }
            else -> binding?.cropIv?.setImageUriAsync(uri)
        }
    }

    override fun onClick(view: View) {
        when (view.id) {
            else -> {
                vibrator.vibrate(PrefsKeys.VIB_LNG)
                val drawable = binding?.cropFbPb?.drawable
                binding?.let {
                    if (drawable is Animatable) {
                        (drawable as Animatable).start()
                        it.cropFbPb.visibility = View.VISIBLE
                    }
                    it.cropIv.getCroppedImageAsync()
                    it.cropFb.isEnabled = false
                }
            }
        }
    }

    override fun rotate() {
        binding?.let { it.cropIv?.rotateImage(90) }
    }

    override fun onCropImageComplete(cropImageView: CropImageView, cropResult: CropImageView.CropResult) {
        if (cropResult.isSuccessful) {
            onSuccess(cropResult.bitmap)
        } else {
            onError()
        }
    }

    private fun onSuccess(bitmap: Bitmap) {
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
        val byteArray = stream.toByteArray()
        presenter?.cropped(byteArray)
        if (!bitmap.isRecycled) {
            bitmap.recycle()
        }
        binding?.cropFb?.isEnabled = true
    }

    private fun onError() {
        presenter?.croppedFail()
        binding?.cropFb?.isEnabled = true
    }
}