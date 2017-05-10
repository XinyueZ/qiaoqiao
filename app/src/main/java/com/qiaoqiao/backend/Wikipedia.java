package com.qiaoqiao.backend;

import com.qiaoqiao.backend.model.wikipedia.WikiResult;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface Wikipedia {
	@GET( ".")
	Observable<WikiResult> getResult1(@Query("action") String action,
	                                 @Query("format") String format,
	                                 @Query("prop") String prop,
	                                 @Query("piprop") String piprop,
	                                 @Query("exintro") boolean exintro,
	                                 @Query("explaintext") boolean explaintext,
	                                 @Query("exlimit") int exlimit,
	                                 @Query("redirects") String redirects,
	                                 @Query("titles") String titles);

	@GET
	Observable<WikiResult> getResult2(@Url String url);
}
