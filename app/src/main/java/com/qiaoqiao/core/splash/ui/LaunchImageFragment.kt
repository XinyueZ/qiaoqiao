package com.qiaoqiao.core.splash.ui

import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.qiaoqiao.core.splash.SplashContract
import com.qiaoqiao.core.splash.SplashPresenter
import com.qiaoqiao.databinding.FragmentLaunchImageBinding
import com.qiaoqiao.utils.SystemUiHelper

class LaunchImageFragment : Fragment(), SplashContract.LaunchImageView {
    private var presenter: SplashContract.Presenter? = null
    private var binding: FragmentLaunchImageBinding? = null

    companion object Factory {
        fun newInstance(cxt: Context): LaunchImageFragment = Fragment.instantiate(cxt, LaunchImageFragment::class.java.name) as LaunchImageFragment
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val uiHelper = SystemUiHelper(activity, SystemUiHelper.LEVEL_IMMERSIVE, 0)
        uiHelper.hide()
        super.onCreate(savedInstanceState)
        val binding = FragmentLaunchImageBinding.inflate(inflater!!, container, false)
        binding.uiHelper = uiHelper
        this.binding = binding
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        presenter?.loadLaunchImage()
    }

    override fun getBinding(): FragmentLaunchImageBinding? = this.binding

    override fun showLaunchImage(uri: Uri) {
        Glide.with(this)
                .load(uri)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .skipMemoryCache(false)
                .crossFade()
                .into(binding?.launchImageIv)
    }

    override fun showLaunchImage(data: ByteArray) {
        binding?.launchImageIv?.setImageBitmap(BitmapFactory.decodeByteArray(data, data.size, 0))
    }

    override fun setPresenter(presenter: SplashPresenter) {
        this.presenter = presenter
    }
}