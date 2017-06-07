package com.qiaoqiao.core.splash

import android.net.Uri
import com.qiaoqiao.databinding.FragmentLaunchImageBinding
import com.qiaoqiao.mvp.BasePresenter
import com.qiaoqiao.mvp.BaseView

class SplashContract {
    interface View : BaseView<SplashPresenter, FragmentLaunchImageBinding> {
        override fun getBinding(): FragmentLaunchImageBinding?
        fun showLaunchImage(uri: Uri): Unit
        fun showLaunchImage(data: ByteArray): Unit
    }

    interface Presenter : BasePresenter {
        fun loadLaunchImage(): Unit
    }
}