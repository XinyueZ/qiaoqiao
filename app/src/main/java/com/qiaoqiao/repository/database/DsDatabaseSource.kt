package com.qiaoqiao.repository.database

import com.google.api.client.json.gson.GsonFactory
import com.google.api.services.vision.v1.model.BatchAnnotateImagesResponse
import com.qiaoqiao.app.App
import com.qiaoqiao.repository.AbstractDsSource
import com.qiaoqiao.repository.DsLoadedCallback
import java.io.IOException

class DsDatabaseSource : AbstractDsSource() {
    override fun onRecentRequest(callback: DsLoadedCallback) {
        App.realm
                .where(HistoryItem::class.java)
                .findAll().forEach {
            try {
                val response = GsonFactory.getDefaultInstance()
                        .createJsonParser(it.jsonText)
                        .parse(BatchAnnotateImagesResponse::class.java)
                callback.onVisionResponse(response)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }
}