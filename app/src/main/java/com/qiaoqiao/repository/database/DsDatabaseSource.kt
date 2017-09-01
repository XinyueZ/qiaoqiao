package com.qiaoqiao.repository.database

import com.google.api.client.json.gson.GsonFactory
import com.google.api.services.vision.v1.model.BatchAnnotateImagesResponse
import com.qiaoqiao.app.App
import com.qiaoqiao.repository.AbstractDsSource
import com.qiaoqiao.repository.DsLoadedCallback
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.disposables.Disposable
import java.io.IOException

class DsDatabaseSource : AbstractDsSource() {
    override fun onRecentRequest(callback: DsLoadedCallback): Disposable {
        val flowable: Flowable<BatchAnnotateImagesResponse> = Flowable.create({ emitter ->
            App.realm
                    .where(HistoryItem::class.java)
                    .findAll().forEach {
                try {
                    val response = GsonFactory.getDefaultInstance()
                            .createJsonParser(it.jsonText)
                            .parse(BatchAnnotateImagesResponse::class.java)
                    emitter.onNext(response)
                } catch (e: IOException) {
                    emitter.onError(e)
                }
            }
        }, BackpressureStrategy.BUFFER)
        return flowable.subscribe(
                { callback.onVisionResponse(it) },
                { callback.onException(it) }
        )
    }
}