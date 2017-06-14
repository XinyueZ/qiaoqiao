package com.qiaoqiao.repository.imageprovider

import android.content.Context
import android.net.Uri
import android.support.v4.content.SharedPreferencesCompat
import android.support.v7.preference.PreferenceManager
import com.qiaoqiao.repository.AbstractDsSource
import com.qiaoqiao.repository.DsLoadedCallback
import com.qiaoqiao.repository.backend.ImageProvider
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class RemoteImageDs(imageProvider: ImageProvider) : AbstractDsSource(imageProvider) {
    override fun onImage(cxt: Context, callback: DsLoadedCallback) {
        imageProvider.getLaunchImages().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ res ->
                    if (res.creatives.isNotEmpty()) {
                        callback.onImageLoad(Uri.parse(res.creatives[0].url))

                        val prefs = PreferenceManager.getDefaultSharedPreferences(cxt)
                        val edit = prefs.edit()
                        edit.putLong("remote-launch-image-time", System.currentTimeMillis())
                        SharedPreferencesCompat.EditorCompat.getInstance().apply(edit)
                    } else callback.onImageLoad(ByteArray(0))
                }, { e ->
                    e.printStackTrace()
                    callback.onImageLoad(ByteArray(0))
                })
    }
}