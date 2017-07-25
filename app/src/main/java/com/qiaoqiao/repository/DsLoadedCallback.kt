package com.qiaoqiao.repository

import android.net.Uri
import android.support.annotation.NonNull
import android.text.TextUtils
import com.google.android.gms.tasks.OnSuccessListener
import com.google.api.services.vision.v1.model.BatchAnnotateImagesResponse
import com.google.api.services.vision.v1.model.Status
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import com.qiaoqiao.repository.backend.model.translate.Data
import com.qiaoqiao.repository.backend.model.wikipedia.WikiResult
import com.qiaoqiao.repository.backend.model.wikipedia.geo.GeoResult
import com.qiaoqiao.utils.LL
import java.io.IOException

abstract class DsLoadedCallback {
    inline fun onSaveHistory(bytes: ByteArray, l: OnSuccessListener<UploadTask.TaskSnapshot>) {
        val currentUser = FirebaseAuth.getInstance()
                .currentUser ?: return
        val rootRef = FirebaseStorage.getInstance().reference
        val imageRef = rootRef.child("images/" + currentUser.uid + "/" + System.currentTimeMillis() + ".jpg")
        val uploadTask = imageRef.putBytes(bytes)
        uploadTask.addOnFailureListener { LL.d("upload image unsuccessfully") }.addOnSuccessListener(l)
    }

    open fun onVisionResponse(response: BatchAnnotateImagesResponse?) {
        if (response == null) {
            LL.d("response is NULL.")
            return
        }
        try {
            LL.d("response is OK.")
            LL.d(response.toPrettyString())
        } catch (e: IOException) {
            LL.d("response is available but IO problem: " + e.toString())
        }

    }

    open fun onKnowledgeResponse(result: WikiResult) {
        LL.d("response of wiki: " + result.toString())
    }

    open fun onGeosearchResponse(@NonNull result: GeoResult) {
        LL.d("response of wiki: " + result.toString())
    }

    open fun onError(@NonNull status: Status) {
        if (!TextUtils.isEmpty(status.message)) {
            LL.e(status.message)
        } else {
            LL.e("Vision API on some error:")
            val details = status.details
            for (mapDetail in details) {
                val mapDetailKeys = mapDetail.keys
                for (key in mapDetailKeys) {
                    LL.e(mapDetail.get(key)
                            .toString())
                }
            }
        }
    }

    open fun onException(@NonNull e: Exception) {
        LL.e(e.toString())
    }

    open fun onTranslateData(@NonNull translateData: Data) {
        LL.d("size of responses of cloud translate: " + translateData.translations.size)
    }

    open fun onImageLoad(@NonNull imageLocation: Uri) {
        LL.d("loaded an image at location: " + imageLocation.toString())

    }

    open fun onImageLoad(@NonNull imageBytes: ByteArray) {
        LL.d("loaded an image with size: " + imageBytes.size)
    }
}