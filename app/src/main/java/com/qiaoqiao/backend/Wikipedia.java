package com.qiaoqiao.backend;

import android.support.annotation.NonNull;

import com.qiaoqiao.backend.model.wikipedia.WikiResult;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.POST;

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


}
