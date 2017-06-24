package com.qiaoqiao.core.splash.ui

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.qiaoqiao.app.PrefsKeys.COMMON_DELAY_SEC
import com.qiaoqiao.core.camera.ui.CameraActivity
import com.qiaoqiao.core.splash.SplashContract
import com.qiaoqiao.core.splash.SplashPresenter
import com.qiaoqiao.databinding.LaunchImageBinding
import com.qiaoqiao.utils.ImageUtils
import com.qiaoqiao.utils.LL
import java.lang.Exception
import java.util.concurrent.TimeUnit

class LaunchImageFragment : Fragment(), SplashContract.LaunchImageView, RequestListener<Uri, Bitmap> {

    private var presenter: SplashContract.Presenter? = null
    private lateinit var binding: LaunchImageBinding

    companion object {
        fun newInstance(cxt: Context): LaunchImageFragment = Fragment.instantiate(cxt, LaunchImageFragment::class.java.name) as LaunchImageFragment
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreate(savedInstanceState)
        val binding = LaunchImageBinding.inflate(inflater!!, container, false)
        this.binding = binding
        return binding.root
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        presenter?. loadLaunchImage (context)
    }

    override fun getBinding(): LaunchImageBinding = this.binding

    override fun showLaunchImage(uri: Uri) {
        Glide.with(this)
                .load(uri).asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .skipMemoryCache(false)
                .crossFade()
                .centerCrop()
                .listener(this)
                .into(binding.launchImageIv)
    }

    override fun onResourceReady(resource: Bitmap?, model: Uri?, target: Target<Bitmap>?, isFromMemoryCache: Boolean, isFirstResource: Boolean): Boolean {
        goToHome()
        if (resource == null) return false
        presenter?.saveLoadedLaunchImage(ImageUtils.convertImage2Bytes(resource))
        return false
    }

    override fun onException(e: Exception?, model: Uri?, target: Target<Bitmap>?, isFirstResource: Boolean): Boolean {
        goToHome()
        return false
    }

    override fun showLaunchImage(data: ByteArray) {
        if (data.isEmpty()) {
            goToHome()
            return
        }
        binding.launchImageIv.setImageBitmap(BitmapFactory.decodeByteArray(data, 0, data.size))
        Handler().postDelayed({ -> goToHome() }, TimeUnit.SECONDS.toMillis(COMMON_DELAY_SEC))
    }

    override fun onStop() {
        super.onStop()
        Glide.clear(binding.launchImageIv)
    }

    override fun setPresenter(presenter: SplashPresenter) {
        this.presenter = presenter
    }

    private fun goToHome() {
        CameraActivity.showInstance(activity)
        activity.finish()
    }
}