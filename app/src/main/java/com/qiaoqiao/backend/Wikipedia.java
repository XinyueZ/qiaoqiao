package com.qiaoqiao.backend;

import com.qiaoqiao.backend.model.wikipedia.WikiResult;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface Wikipedia {
	@GET
	Observable<WikiResult> getResult1(@Url String url);

	@GET
	Observable<WikiResult> getResult2(@Url String url);
}
