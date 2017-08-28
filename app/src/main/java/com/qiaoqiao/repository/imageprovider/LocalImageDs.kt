package com.qiaoqiao.repository.imageprovider

import android.content.Context
import com.qiaoqiao.app.App
import com.qiaoqiao.repository.AbstractDsSource
import com.qiaoqiao.repository.DsLoadedCallback
import com.qiaoqiao.repository.backend.ImageProvider
import com.qiaoqiao.repository.database.LastLaunchImage
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.disposables.Disposable

class LocalImageDs(imageProvider: ImageProvider) : AbstractDsSource(imageProvider) {
    override fun onImage(cxt: Context, callback: DsLoadedCallback): Disposable {
        val flowable: Flowable<ByteArray> = Flowable.create({ emitter ->
            val res = App.realm
                    .where(LastLaunchImage::class.java)
                    .findAll()
            if (res.isLoaded && res.size > 0) {
                emitter.onNext(res[0]
                        .byteArray)
            } else {
                emitter.onNext(ByteArray(0))
            }
        }, BackpressureStrategy.BUFFER)
        return flowable.subscribe(
                {
                    callback.onImageLoad(it)
                }
        )
    }
}