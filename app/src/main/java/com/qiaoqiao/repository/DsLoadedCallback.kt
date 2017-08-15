package com.qiaoqiao.repository

import android.net.Uri
import android.text.TextUtils
import com.google.api.services.vision.v1.model.BatchAnnotateImagesResponse
import com.google.api.services.vision.v1.model.Status
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import com.qiaoqiao.core.product.model.ProductEntity
import com.qiaoqiao.repository.backend.model.translate.Data
import com.qiaoqiao.repository.backend.model.wikipedia.WikiResult
import com.qiaoqiao.repository.backend.model.wikipedia.geo.GeoResult
import com.qiaoqiao.repository.database.HistoryItem
import com.qiaoqiao.utils.LL
import io.realm.Realm
import java.io.IOException

abstract class DsLoadedCallback {
    inline fun pushOnFirebase(bytes: ByteArray, noinline err: (Exception) -> Unit, crossinline succ: (UploadTask.TaskSnapshot) -> Unit) {
        val currentUser = FirebaseAuth.getInstance()
                .currentUser ?: return
        val rootRef = FirebaseStorage.getInstance().reference
        val imageRef = rootRef.child("images/" + currentUser.uid + "/" + System.currentTimeMillis() + ".jpg")
        val uploadTask = imageRef.putBytes(bytes)
        uploadTask.addOnFailureListener { err(it) }.addOnSuccessListener { succ(it) }
    }

    fun saveOnLocalHistory(imageUri: Uri, json: String) {
        val realm = Realm.getDefaultInstance()
        realm.executeTransactionAsync({ bgRealm ->
            val historyItem = bgRealm.createObject(HistoryItem::class.java)
            historyItem.imageUri = imageUri.toString()
            historyItem.jsonText = json
            historyItem.savedTime = System.currentTimeMillis()
        }, { LL.d("Saved history successfully.") }, { LL.d("Saved history fail.") })
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

    open fun onGeosearchResponse(result: GeoResult) {
        LL.d("response of wiki: " + result.toString())
    }

    open fun onVisionApiError(status: Status) {
        if (!TextUtils.isEmpty(status.message)) {
            LL.e(status.message)
        } else {
            LL.e("Vision API on some error:")
            val details = status.details
            for (mapDetail in details) {
                val mapDetailKeys = mapDetail.keys
                for (key in mapDetailKeys) {
                    LL.e(mapDetail[key]
                            .toString())
                }
            }
        }
    }

    open fun onKnowledgeResponse(products: List<ProductEntity>) {
    }

    open fun onException(e: Exception) {
        LL.e(e.toString())
    }

    open fun onTranslateData(translateData: Data) {
        LL.d("size of responses of cloud translate: " + translateData.translations.size)
    }

    open fun onImageLoad(imageLocation: Uri) {
        LL.d("loaded an image at location: " + imageLocation.toString())

    }

    open fun onImageLoad(imageBytes: ByteArray) {
        LL.d("loaded an image with size: " + imageBytes.size)
    }

    open fun onSomeThingSuccessfully() {

    }

    open fun onSomeThingUnsuccessfully() {

    }
}