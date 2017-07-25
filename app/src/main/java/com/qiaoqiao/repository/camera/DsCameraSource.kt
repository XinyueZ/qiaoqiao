package com.qiaoqiao.repository.camera

import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.storage.UploadTask
import com.qiaoqiao.repository.AbstractDsSource
import com.qiaoqiao.repository.DsLoadedCallback
import com.qiaoqiao.repository.annotation.RepositoryScope
import com.qiaoqiao.repository.backend.Google

@RepositoryScope
class DsCameraSource(google: Google) : AbstractDsSource(google) {
    override fun onBytes(bytes: ByteArray, callback: DsLoadedCallback) {
        callback.onSaveHistory(bytes, OnSuccessListener<UploadTask.TaskSnapshot> {
            it.downloadUrl?.let {
                google.getAnnotateImageResponse(Google.UriImageBuilder.newBuilder(it)) { response ->
                    when {
                        response.responses[0].error != null -> callback.onError(response.responses[0].error)
                        else -> callback.onVisionResponse(response)
                    }
                }
            }
        })
    }
}