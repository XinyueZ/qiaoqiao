package com.qiaoqiao.repository.backend;

import android.support.annotation.NonNull;

import com.qiaoqiao.repository.backend.model.wikipedia.WikiResult;
import com.qiaoqiao.repository.backend.model.wikipedia.geo.GeoResult;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Url;

public interface Wikipedia {
	final class WikiReqBody {
		@NonNull String language;
		@NonNull String keyword;

		public WikiReqBody(@NonNull String language, @NonNull String keyword) {
			this.language = language;
			this.keyword = keyword;
		}
	}

	@POST("documents/wikipedia")
	Observable<WikiResult> getResult(@Body WikiReqBody body);

	@GET
	Observable<GeoResult> getGeosearch(@Url String url);
}
