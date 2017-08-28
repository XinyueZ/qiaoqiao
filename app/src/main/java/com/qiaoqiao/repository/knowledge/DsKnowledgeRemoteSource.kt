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
import com.qiaoqiao.rx.Composer
import io.reactivex.Flowable
import java.util.*

class DsKnowledgeRemoteSource(private val key: Key, google: Google, wikipedia: Wikipedia, productsService: ProductsService) : AbstractDsSource(google, wikipedia, productsService) {
    override fun onTranslate(q: String, callback: DsLoadedCallback) {
        google?.let {
            it.translateService
                    .translate(q,
                            Locale.getDefault().language,
                            "text",
                            key.toString())
                    .compose(Composer())
                    .subscribe { callback.onTranslateData(it.data) }
        }
    }

    override fun onKnowledgeQuery(keyword: String, callback: DsLoadedCallback) {
        wikipedia?.let {
            it.getResult(KnowledgeRequest(Locale.getDefault()
                    .language, keyword))
                    .compose(Composer())
                    .subscribe {
                        try {
                            if (it.query
                                    .pages
                                    .list
                                    .size > 0) {
                                callback.onKnowledgeResponse(it)
                            }
                        } catch (e: Exception) {
                            callback.onException(e)
                        }
                    }
        }
    }

    override fun onKnowledgeQuery(langLink: LangLink, callback: DsLoadedCallback) {
        wikipedia?.let {
            it.getResult(KnowledgeRequest(langLink.language, langLink.query))
                    .compose(Composer())
                    .subscribe {
                        try {
                            if (it.query
                                    .pages
                                    .list
                                    .size > 0) {
                                callback.onKnowledgeResponse(it)
                            }
                        } catch (e: Exception) {
                            callback.onException(e)
                        }
                    }
        }
    }

    override fun onKnowledgeQuery(pageId: Int, callback: DsLoadedCallback) {
        wikipedia?.let {
            it.getResult(wikiQuery(Locale.getDefault()
                    .language, pageId.toString() + ""))
                    .compose(Composer())
                    .subscribe({
                        try {
                            if (!it.query.pages.list.isEmpty()) {
                                callback.onKnowledgeResponse(it)
                            }
                        } catch (e: Exception) {
                            callback.onException(e)
                        }
                    })
        }
    }

    override fun onGeosearchQuery(latLng: LatLng, radius: Long, callback: DsLoadedCallback) {
        wikipedia?.let {
            val geoLoc = String.format("%s|%s", latLng.latitude.toString(), latLng.longitude.toString())
            it.getGeosearch(wikiGeosearch(Locale.getDefault()
                    .language, radius, geoLoc))
                    .compose(Composer())
                    .subscribe {
                        try {
                            if (it.query != null) {
                                callback.onGeosearchResponse(it)
                            }
                        } catch (e: Exception) {
                            callback.onException(e)
                        }
                    }
        }
    }

    override fun onKnowledgeQuery(barcode: Barcode, callback: DsLoadedCallback) {
        productsService?.let {
            it.getProducts(KnowledgeRequest(Locale.getDefault().language, barcode.rawValue)).compose(Composer())
                    .subscribe {
                        try {
                            callback.onKnowledgeResponse(
                                    Flowable.just(it).flatMapIterable { it.result }.map { ProductEntity(it) }.toList().blockingGet())
                        } catch (e: Exception) {
                            callback.onException(e)
                        }
                    }
        }
    }
}

private fun wikiQuery(lang: String, keyword: String) = wikiHost(lang) + wikiQuery(keyword)
private fun wikiHost(lang: String) = String.format("https://%s.wikipedia.org", lang)
private fun wikiQuery(keyword: String) = "/w/api.php?format=json&action=query&prop=extracts|pageimages|langlinks&llprop=autonym&lldir=descending&lllimit=500&piprop=original|name|thumbnail&exlimit=1&redirects=titles&pageids=" + keyword
private fun wikiGeosearch(lang: String, radius: Long, keyword: String) = wikiHost(lang) + wikiGeosearch(radius, keyword)
private fun wikiGeosearch(radius: Long, keyword: String) = "/w/api.php?format=json&action=query&list=geosearch&gsradius=$radius&gslimit=max&gscoord=$keyword"
