package com.qiaoqiao.core.splash

import android.content.Context
import android.net.Uri
import com.qiaoqiao.databinding.LaunchImageBinding
import com.qiaoqiao.mvp.BasePresenter
import com.qiaoqiao.mvp.BaseView
import com.qiaoqiao.repository.DsLoadedCallback

class SplashContract {
    interface LaunchImageView : BaseView<SplashPresenter, LaunchImageBinding> {
        override fun getBinding(): LaunchImageBinding
        fun showLaunchImage(uri: Uri)
        fun showLaunchImage(data: ByteArray)
    }

    interface Presenter : BasePresenter {
        fun loadLaunchImage(cxt: Context)
        fun saveLoadedLaunchImage(imageData: ByteArray, callback: DsLoadedCallback)
    }
}