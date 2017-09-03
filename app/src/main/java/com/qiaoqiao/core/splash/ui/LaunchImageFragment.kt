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
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.qiaoqiao.app.COMMON_DELAY_SEC
import com.qiaoqiao.app.GlideApp
import com.qiaoqiao.core.camera.ui.CameraActivity
import com.qiaoqiao.core.splash.SplashContract
import com.qiaoqiao.core.splash.SplashPresenter
import com.qiaoqiao.databinding.LaunchImageBinding
import com.qiaoqiao.repository.DsLoadedCallback
import com.qiaoqiao.utils.DeviceUtils
import com.qiaoqiao.utils.ImageUtils
import com.qiaoqiao.utils.LL
import java.util.concurrent.TimeUnit

class LaunchImageFragment : Fragment(), SplashContract.LaunchImageView, RequestListener<Bitmap> {

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
        presenter?.loadLaunchImage(context)
    }

    override fun getBinding(): LaunchImageBinding = this.binding

    override fun showLaunchImage(uri: Uri) {
        val screenSize = DeviceUtils.getScreenSize(context)
        val imageUri = Uri.parse(uri.toString() + "/" + screenSize.Width + "x" + screenSize.Height)
        LL.d(imageUri.toString())
        GlideApp.with(this).asBitmap()
                .load(imageUri)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .skipMemoryCache(false)
                .centerCrop()
                .listener(this)
                .into(binding.launchImageIv)
    }

    override fun showLaunchImage(data: ByteArray) {
        if (data.isEmpty()) {
            goToHome()
            return
        }
        binding.launchImageIv.setImageBitmap(BitmapFactory.decodeByteArray(data, 0, data.size))
        Handler().postDelayed({ goToHome() }, TimeUnit.SECONDS.toMillis(COMMON_DELAY_SEC))
    }

    override fun onStop() {
        super.onStop()
        Glide.with(binding.launchImageIv).clear(binding.launchImageIv)
    }

    override fun setPresenter(presenter: SplashPresenter) {
        this.presenter = presenter
    }

    private fun goToHome() {
        CameraActivity.showInstance(activity, true)
        activity.finish()
    }

    override fun onResourceReady(resource: Bitmap?, model: Any?, target: Target<Bitmap>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
        if (resource == null) return false
        presenter?.saveLoadedLaunchImage(ImageUtils.convertImage2Bytes(resource), object : DsLoadedCallback() {
            override fun onSomeThingSuccessfully() {
                goToHome()
            }

            override fun onSomeThingUnsuccessfully() {
                goToHome()
            }
        })
        return false
    }

    override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Bitmap>?, isFirstResource: Boolean): Boolean {
        goToHome()
        return false
    }
}