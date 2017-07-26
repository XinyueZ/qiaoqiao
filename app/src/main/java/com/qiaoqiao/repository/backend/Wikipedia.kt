package com.qiaoqiao.repository.backend

import com.qiaoqiao.repository.backend.model.wikipedia.WikiResult
import com.qiaoqiao.repository.backend.model.wikipedia.geo.GeoResult
import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Url

interface Wikipedia {

    @POST("documents/wikipedia")
    fun getResult(@Body body: WikiReqBody): Observable<WikiResult>

    @GET
    fun getResult(@Url queryWithPageId: String): Observable<WikiResult>

    @GET
    fun getGeosearch(@Url url: String): Observable<GeoResult>

    class WikiReqBody(val language: String, val keyword: String)
}
