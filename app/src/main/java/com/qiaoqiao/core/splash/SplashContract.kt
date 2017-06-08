package com.qiaoqiao.core.splash

import android.net.Uri
import com.qiaoqiao.databinding.FragmentLaunchImageBinding
import com.qiaoqiao.mvp.BasePresenter
import com.qiaoqiao.mvp.BaseView

class SplashContract {
    interface LaunchImageView : BaseView<SplashPresenter, FragmentLaunchImageBinding> {
        override fun getBinding(): FragmentLaunchImageBinding?
        fun showLaunchImage(uri: Uri)
        fun showLaunchImage(data: ByteArray)
        fun requirePermission()
    }

    interface Presenter : BasePresenter {
        fun loadLaunchImage()
    }
}