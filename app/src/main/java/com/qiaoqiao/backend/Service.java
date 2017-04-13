package com.qiaoqiao.backend;

import com.qiaoqiao.backend.model.request.AnnotateImageRequest;
import com.qiaoqiao.backend.model.response.AnnotateImageResponseCollection;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface Service {
	@POST("images:annotate")
	Observable<AnnotateImageResponseCollection> getAnnotateImageResponse(@Query("key") int key, @Body AnnotateImageRequest request);


}
