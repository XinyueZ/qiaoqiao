package com.qiaoqiao.repository.knowledge

import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.vision.barcode.Barcode
import com.qiaoqiao.app.Key
import com.qiaoqiao.core.product.model.ProductEntity
import com.qiaoqiao.repository.AbstractDsSource
import com.qiaoqiao.repository.DsLoadedCallback
import com.qiaoqiao.repository.backend.Google
import com.qiaoqiao.repository.backend.KnowledgeRequest
import com.qiaoqiao.repository.backend.ProductsService
import com.qiaoqiao.repository.backend.Wikipedia
import com.qiaoqiao.repository.backend.model.wikipedia.LangLink
import com.qiaoqiao.rx.IoToMainScheduleObservable
import io.reactivex.disposables.Disposable
import java.util.*

class DsKnowledgeRemoteSource(private val key: Key, google: Google, wikipedia: Wikipedia, productsService: ProductsService) : AbstractDsSource(google, wikipedia, productsService) {
    override fun onTranslate(q: String, callback: DsLoadedCallback): Disposable {
        google?.let {
            return (it.translateService
                    .translate(q,
                            Locale.getDefault().language,
                            "text",
                            key.toString())
                    .compose(IoToMainScheduleObservable())
                    .subscribe({ callback.onTranslateData(it.data) }, { callback.onException(it) }))
        } ?: run {
            throw NotImplementedError()
        }
    }

    override fun onKnowledgeQuery(keyword: String, callback: DsLoadedCallback): Disposable {
        wikipedia?.let {
            return (it.getResult(KnowledgeRequest(Locale.getDefault()
                    .language, keyword))
                    .compose(IoToMainScheduleObservable())
                    .subscribe({
                        if (it.query
                                .pages
                                .list
                                .size > 0) {
                            callback.onKnowledgeResponse(it)
                        }
                    }, { callback.onException(it) }))
        } ?: run {
            throw NotImplementedError()
        }
    }

    override fun onKnowledgeQuery(langLink: LangLink, callback: DsLoadedCallback): Disposable {
        wikipedia?.let {
            return (it.getResult(KnowledgeRequest(langLink.language, langLink.query))
                    .compose(IoToMainScheduleObservable())
                    .subscribe({
                        if (it.query
                                .pages
                                .list
                                .size > 0) {
                            callback.onKnowledgeResponse(it)
                        }
                    }, { callback.onException(it) }))
        } ?: run {
            throw NotImplementedError()
        }
    }

    override fun onKnowledgeQuery(pageId: Int, callback: DsLoadedCallback): Disposable {
        wikipedia?.let {
            return (it.getResult(wikiQuery(Locale.getDefault()
                    .language, pageId.toString() + ""))
                    .compose(IoToMainScheduleObservable())
                    .subscribe({
                        if (!it.query.pages.list.isEmpty()) {
                            callback.onKnowledgeResponse(it)
                        }
                    }, { callback.onException(it) }))
        } ?: run {
            throw NotImplementedError()
        }
    }

    override fun onGeosearchQuery(latLng: LatLng, radius: Long, callback: DsLoadedCallback): Disposable {
        wikipedia?.let {
            val geoLoc = String.format("%s|%s", latLng.latitude.toString(), latLng.longitude.toString())
            return (it.getGeosearch(wikiGeosearch(Locale.getDefault()
                    .language, radius, geoLoc))
                    .compose(IoToMainScheduleObservable())
                    .subscribe({
                        if (it.query != null) {
                            callback.onGeosearchResponse(it)
                        }
                    }, { callback.onException(it) }))
        } ?: run {
            throw NotImplementedError()
        }
    }

    override fun onKnowledgeQuery(barcode: Barcode, callback: DsLoadedCallback): Disposable {
        productsService?.let {
            return (it.getProduct(KnowledgeRequest(Locale.getDefault().language, barcode.rawValue)).compose(IoToMainScheduleObservable())
                    .subscribe({
                        callback.onKnowledgeResponse(ProductEntity(it))
                    }, { callback.onException(it) }))
        } ?: run {
            throw NotImplementedError()
        }
    }
}

private fun wikiQuery(lang: String, keyword: String) = wikiHost(lang) + wikiQuery(keyword)
private fun wikiHost(lang: String) = String.format("https://%s.wikipedia.org", lang)
private fun wikiQuery(keyword: String) = "/w/api.php?format=json&action=query&prop=extracts|pageimages|langlinks&llprop=autonym&lldir=descending&lllimit=500&piprop=original|name|thumbnail&exlimit=1&redirects=titles&pageids=" + keyword
private fun wikiGeosearch(lang: String, radius: Long, keyword: String) = wikiHost(lang) + wikiGeosearch(radius, keyword)
private fun wikiGeosearch(radius: Long, keyword: String) = "/w/api.php?format=json&action=query&list=geosearch&gsradius=$radius&gslimit=max&gscoord=$keyword"
