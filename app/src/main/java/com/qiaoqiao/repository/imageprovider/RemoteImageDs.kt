package com.qiaoqiao.repository.imageprovider

import android.net.Uri
import com.qiaoqiao.repository.AbstractDsSource
import com.qiaoqiao.repository.DsLoadedCallback
import com.qiaoqiao.repository.backend.ImageProvider
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class RemoteImageDs(imageProvider: ImageProvider) : AbstractDsSource(imageProvider) {
    override fun onImage(callback: DsLoadedCallback) {
        imageProvider.getLaunchImages().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ res ->
                    if (res.creatives.isNotEmpty()) callback.onImageLoad(Uri.parse(res.creatives[0].url))
                })
    }
}