package com.qiaoqiao.repository.camera

import com.qiaoqiao.repository.AbstractDsSource
import com.qiaoqiao.repository.DsLoadedCallback
import com.qiaoqiao.repository.annotation.RepositoryScope
import com.qiaoqiao.repository.backend.Google

@RepositoryScope
class DsCameraSource(google: Google) : AbstractDsSource(google) {
    override fun onBytes(bytes: ByteArray, callback: DsLoadedCallback) {
        callback.pushOnFirebase(bytes, { callback.onException(it) }) {
            it.downloadUrl?.let {
                val uri = it
                google?.getAnnotateImageResponse(Google.UriImageBuilder.newBuilder(uri)) {
                    when {
                        it.responses[0].error != null -> callback.onVisionApiError(it.responses[0].error)
                        else -> {
                            callback.onVisionResponse(it)
                            callback.saveOnLocalHistory(uri, it.toPrettyString())
                        }
                    }
                }
            }
        }
    }
}