package com.qiaoqiao.repository.knowledge

import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.vision.barcode.Barcode
import com.qiaoqiao.app.Key
import com.qiaoqiao.repository.AbstractDsSource
import com.qiaoqiao.repository.DsLoadedCallback
import com.qiaoqiao.repository.backend.Google
import com.qiaoqiao.repository.backend.ProductsService
import com.qiaoqiao.repository.backend.Wikipedia
import com.qiaoqiao.repository.backend.model.wikipedia.LangLink
import com.qiaoqiao.rx.Composer
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
                    .subscribe { response -> callback.onTranslateData(response.data) }
        }
    }

    override fun onKnowledgeQuery(keyword: String, callback: DsLoadedCallback) {
        wikipedia?.let {
            it.getResult(Wikipedia.WikiReqBody(Locale.getDefault()
                    .language, keyword))
                    .compose(Composer())
                    .subscribe { result1 ->
                        try {
                            if (result1.query
                                    .pages
                                    .list
                                    .size > 0) {
                                callback.onKnowledgeResponse(result1)
                            }
                        } catch (e: Exception) {
                            callback.onException(e)
                        }
                    }
        }
    }

    override fun onKnowledgeQuery(langLink: LangLink, callback: DsLoadedCallback) {
        wikipedia?.let {
            it.getResult(Wikipedia.WikiReqBody(langLink.language, langLink.query))
                    .compose(Composer())
                    .subscribe { result1 ->
                        try {
                            if (result1.query
                                    .pages
                                    .list
                                    .size > 0) {
                                callback.onKnowledgeResponse(result1)
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
                    .subscribe({ result1 ->
                        try {
                            if (!result1.query.pages.list.isEmpty()) {
                                callback.onKnowledgeResponse(result1)
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
                    .subscribe { result1 ->
                        try {
                            if (result1.query != null) {
                                callback.onGeosearchResponse(result1)
                            }
                        } catch (e: Exception) {
                            callback.onException(e)
                        }
                    }
        }
    }

    override fun onKnowledgeQuery(barcode: Barcode, callback: DsLoadedCallback) {
    }
}

private fun wikiQuery(lang: String, keyword: String) = wikiHost(lang) + wikiQuery(keyword)
private fun wikiHost(lang: String) = String.format("https://%s.wikipedia.org", lang)
private fun wikiQuery(keyword: String) = "/w/api.php?format=json&action=query&prop=extracts|pageimages|langlinks&llprop=autonym&lldir=descending&lllimit=500&piprop=original|name|thumbnail&exlimit=1&redirects=titles&pageids=" + keyword
private fun wikiGeosearch(lang: String, radius: Long, keyword: String) = wikiHost(lang) + wikiGeosearch(radius, keyword)
private fun wikiGeosearch(radius: Long, keyword: String) = "/w/api.php?format=json&action=query&list=geosearch&gsradius=$radius&gslimit=max&gscoord=$keyword"
