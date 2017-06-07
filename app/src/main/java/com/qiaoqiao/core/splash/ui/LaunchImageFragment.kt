package com.qiaoqiao.core.splash.ui

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.qiaoqiao.core.splash.SplashContract
import com.qiaoqiao.core.splash.SplashPresenter
import com.qiaoqiao.databinding.FragmentLaunchImageBinding
import com.qiaoqiao.utils.SystemUiHelper

class LaunchImageFragment : Fragment(), SplashContract.LaunchImageView {
    private var presenter: SplashContract.Presenter? = null
    private var binding: FragmentLaunchImageBinding? = null

    companion object Factory {
        fun newInstance(cxt: Context): LaunchImageFragment = Fragment.instantiate(cxt, LaunchImageFragment.javaClass.name) as LaunchImageFragment
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
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showLaunchImage(data: ByteArray) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun setPresenter(presenter: SplashPresenter) {
        this.presenter = presenter
    }
}