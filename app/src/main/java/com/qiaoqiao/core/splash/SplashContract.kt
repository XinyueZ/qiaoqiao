package com.qiaoqiao.core.splash

import android.net.Uri
import com.qiaoqiao.databinding.LaunchImageBinding
import com.qiaoqiao.mvp.BasePresenter
import com.qiaoqiao.mvp.BaseView

class SplashContract {
    interface LaunchImageView : BaseView<SplashPresenter, LaunchImageBinding> {
        override fun getBinding(): LaunchImageBinding
        fun showLaunchImage(uri: Uri)
        fun showLaunchImage(data: ByteArray)
        fun requirePermission()
    }

    interface Presenter : BasePresenter {
        fun loadLaunchImage()
    }
}