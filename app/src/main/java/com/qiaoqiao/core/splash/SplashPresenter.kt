package com.qiaoqiao.core.splash

import android.content.Context
import android.net.Uri
import android.support.v4.app.FragmentActivity
import com.qiaoqiao.repository.DsLoadedCallback
import com.qiaoqiao.repository.DsRepository
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import javax.inject.Inject

class SplashPresenter @Inject constructor(private var launchImageView: SplashContract.LaunchImageView,
                                          private var dsRepository: DsRepository)
    : SplashContract.Presenter {
    private val compositeDisposable = CompositeDisposable()

    private fun addToAutoDispose(vararg disposables: Disposable) {
        compositeDisposable.addAll(*disposables)
    }

    private fun autoDispose() {
        compositeDisposable.clear()
    }

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

    private val loadLaunchImageCallback: LoadLaunchImageCallback
        get() = LoadLaunchImageCallback(launchImageView)

    @Inject
    fun onInjected() {
        launchImageView.setPresenter(this)
    }

    override fun loadLaunchImage(cxt: Context) {
        addToAutoDispose(dsRepository.onImage(cxt, loadLaunchImageCallback))
    }

    override fun saveLoadedLaunchImage(imageData: ByteArray, callback: DsLoadedCallback) {
        addToAutoDispose(dsRepository.onLoadedLaunchImage(imageData, callback))
    }

    override fun begin(hostActivity: FragmentActivity) {
    }

    override fun end(hostActivity: FragmentActivity) {
        autoDispose()
    }
}