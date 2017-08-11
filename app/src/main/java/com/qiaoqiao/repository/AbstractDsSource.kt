package com.qiaoqiao.repository

import android.content.Context
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.vision.barcode.Barcode
import com.qiaoqiao.repository.backend.Google
import com.qiaoqiao.repository.backend.ImageProvider
import com.qiaoqiao.repository.backend.ProductsService
import com.qiaoqiao.repository.backend.Wikipedia
import com.qiaoqiao.repository.backend.model.wikipedia.LangLink
import com.qiaoqiao.repository.database.LastLaunchImage
import io.realm.Realm

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

    fun onLoadedLaunchImage(imageData: ByteArray, callback: DsLoadedCallback) {
        val realm = Realm.getDefaultInstance()
        realm.executeTransactionAsync({
            it.delete(LastLaunchImage::class.java)
            val lastLaunchImage = it.createObject(LastLaunchImage::class.java)
            lastLaunchImage.byteArray = imageData
        }, { callback.onSomeThingUnsuccessfully() }, { callback.onSomeThingSuccessfully() })
    }

    open fun onBytes(bytes: ByteArray, callback: DsLoadedCallback) {}

    open fun onKnowledgeQuery(pageId: Int, callback: DsLoadedCallback) {

    }

    open fun onKnowledgeQuery(keyword: String, callback: DsLoadedCallback) {

    }

    open fun onKnowledgeQuery(langLink: LangLink, callback: DsLoadedCallback) {

    }

    open fun onKnowledgeQuery(barcode: Barcode, callback: DsLoadedCallback) {

    }

    open fun onGeosearchQuery(latLng: LatLng, radius: Long, callback: DsLoadedCallback) {

    }

    open fun onTranslate(q: String, callback: DsLoadedCallback) {

    }

    open fun onRecentRequest(callback: DsLoadedCallback) {

    }

    open fun onImage(cxt: Context, callback: DsLoadedCallback) {

    }
}