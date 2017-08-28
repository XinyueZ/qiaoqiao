package com.qiaoqiao.repository.camera

import com.google.api.services.vision.v1.model.BatchAnnotateImagesResponse
import com.qiaoqiao.repository.AbstractDsSource
import com.qiaoqiao.repository.DsLoadedCallback
import com.qiaoqiao.repository.annotation.RepositoryScope
import com.qiaoqiao.repository.backend.Google
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.disposables.Disposable

@RepositoryScope
class DsCameraSource(google: Google) : AbstractDsSource(google) {
    override fun onBytes(bytes: ByteArray, callback: DsLoadedCallback): Disposable {
        val r: Flowable<BatchAnnotateImagesResponse> = Flowable.create({ emitter ->
            callback.pushOnFirebase(bytes, { callback.onException(it) }) {
                it.downloadUrl?.let {
                    val uri = it
                    google?.let {
                        (it.getAnnotateImageResponse(Google.UriImageBuilder.newBuilder(uri), {
                            when {
                                it.responses[0].error != null -> emitter.onError(Throwable(it.responses[0].error.message))
                                else -> {
                                    callback.saveOnLocalHistory(uri, it.toPrettyString())
                                    emitter.onNext(it)
                                }
                            }
                        }))
                    }
                }
            }
        }, BackpressureStrategy.BUFFER)

        return r.subscribe({
            callback.onVisionResponse(it)
        }, {
            callback.onException(it)
        })
    }
}