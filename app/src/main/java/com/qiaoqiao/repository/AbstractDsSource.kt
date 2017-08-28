package com.qiaoqiao.repository

import android.content.Context
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.vision.barcode.Barcode
import com.qiaoqiao.app.App
import com.qiaoqiao.repository.backend.Google
import com.qiaoqiao.repository.backend.ImageProvider
import com.qiaoqiao.repository.backend.ProductsService
import com.qiaoqiao.repository.backend.Wikipedia
import com.qiaoqiao.repository.backend.model.wikipedia.LangLink
import com.qiaoqiao.repository.database.LastLaunchImage
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.disposables.Disposable

abstract class AbstractDsSource() {
    protected var google: Google? = null
    protected var wikipedia: com.qiaoqiao.repository.backend.Wikipedia? = null
    protected var imageProvider: ImageProvider? = null
    protected var productsService: ProductsService? = null

    protected constructor(google: Google) : this() {
        this.google = google
    }

    protected constructor(imageProvider: ImageProvider) : this() {
        this.imageProvider = imageProvider
    }

    protected constructor(google: Google, wikipedia: Wikipedia, productsService: ProductsService) : this() {
        this.google = google
        this.wikipedia = wikipedia
        this.productsService = productsService
    }

    fun onLoadedLaunchImage(imageData: ByteArray, callback: DsLoadedCallback): Disposable {
        val flowable: Flowable<ByteArray> = Flowable.create({ emitter ->
            App.realm.executeTransaction {
                it.delete(LastLaunchImage::class.java)
                val lastLaunchImage = it.createObject(LastLaunchImage::class.java)
                lastLaunchImage.byteArray = imageData
                emitter.onNext(imageData)
            }
        }, BackpressureStrategy.BUFFER)
        return flowable.subscribe({
            callback.onSomeThingSuccessfully()
        }, { callback.onSomeThingUnsuccessfully() })
    }

    open fun onBytes(bytes: ByteArray, callback: DsLoadedCallback): Disposable {
        throw NotImplementedError()
    }

    open fun onKnowledgeQuery(pageId: Int, callback: DsLoadedCallback): Disposable {
        throw NotImplementedError()
    }

    open fun onKnowledgeQuery(keyword: String, callback: DsLoadedCallback): Disposable {
        throw NotImplementedError()
    }

    open fun onKnowledgeQuery(langLink: LangLink, callback: DsLoadedCallback): Disposable {
        throw NotImplementedError()
    }

    open fun onKnowledgeQuery(barcode: Barcode, callback: DsLoadedCallback): Disposable {
        throw NotImplementedError()
    }

    open fun onGeosearchQuery(latLng: LatLng, radius: Long, callback: DsLoadedCallback): Disposable {
        throw NotImplementedError()
    }

    open fun onTranslate(q: String, callback: DsLoadedCallback): Disposable {
        throw NotImplementedError()
    }

    open fun onRecentRequest(callback: DsLoadedCallback): Disposable {
        throw NotImplementedError()
    }

    open fun onImage(cxt: Context, callback: DsLoadedCallback): Disposable {
        throw NotImplementedError()
    }
}