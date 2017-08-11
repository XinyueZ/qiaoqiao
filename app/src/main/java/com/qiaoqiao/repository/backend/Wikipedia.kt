package com.qiaoqiao.repository.backend

import com.qiaoqiao.repository.backend.model.wikipedia.WikiResult
import com.qiaoqiao.repository.backend.model.wikipedia.geo.GeoResult
import io.reactivex.Flowable
import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Url

interface Wikipedia {

    @POST("documents/wikipedia")
    fun getResult(@Body body: WikiReqBody): Flowable<WikiResult>

    @GET
    fun getResult(@Url queryWithPageId: String): Flowable<WikiResult>

    @GET
    fun getGeosearch(@Url url: String): Flowable<GeoResult>

    class WikiReqBody(val language: String, val keyword: String)
}
