package com.qiaoqiao.repository.backend

import com.qiaoqiao.repository.backend.model.zhihu.LaunchImages
import io.reactivex.Observable
import retrofit2.http.GET

interface ImageProvider {
    @GET("prefetch-launch-images/1080*1920")
    fun getLaunchImages(): Observable<LaunchImages>
}