package com.qiaoqiao.repository.backend

import com.qiaoqiao.repository.backend.model.zhihu.LaunchImages
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Url

interface ImageProvider {
    @GET("prefetch-launch-images/1080*1920")
    fun getLaunchImages(@Url url: String): Observable<LaunchImages>
}