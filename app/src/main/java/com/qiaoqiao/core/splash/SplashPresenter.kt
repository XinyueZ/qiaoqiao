package com.qiaoqiao.core.splash

import android.net.Uri
import android.support.v4.app.FragmentActivity
import com.qiaoqiao.repository.DsLoadedCallback
import com.qiaoqiao.repository.DsRepository
import javax.inject.Inject

class SplashPresenter @Inject constructor(private var launchImageView: SplashContract.LaunchImageView, private var dsRepository: DsRepository) : SplashContract.Presenter {
    private class LoadLaunchImageCallback(val launchImageView: SplashContract.LaunchImageView) : DsLoadedCallback() {
        override fun onImageLoad(imageLocation: Uri) {
            super.onImageLoad(imageLocation)
            launchImageView.showLaunchImage(imageLocation)
        }

        override fun onImageLoad(imageBytes: ByteArray) {
            super.onImageLoad(imageBytes)
            launchImageView.showLaunchImage(imageBytes)
        }
    }

    private var loadLaunchImageCallback: LoadLaunchImageCallback

    @Inject
    fun onInjected() {
        launchImageView.setPresenter(this)
    }

    override fun loadLaunchImage() {
        dsRepository.onImage(loadLaunchImageCallback)
    }

    override fun begin(hostActivity: FragmentActivity) {
    }

    override fun end(hostActivity: FragmentActivity) {
    }

    init {
        loadLaunchImageCallback = LoadLaunchImageCallback(launchImageView)
    }

}