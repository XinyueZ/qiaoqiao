package com.qiaoqiao.backend;

import com.qiaoqiao.backend.model.wikipedia.WikiResult;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WikipediaAPIs {
	@GET("w/api.php")
	Observable<WikiResult> getResult(@Query("action") String action,
	                                 @Query("format") String format,
	                                 @Query("prop") String prop,
	                                 @Query("exintro") boolean exintro,
	                                 @Query("explaintext") boolean explaintext,
	                                 @Query("exlimit") int exlimit,
	                                 @Query("redirects") String redirects,
	                                 @Query("titles") String titles);
}
