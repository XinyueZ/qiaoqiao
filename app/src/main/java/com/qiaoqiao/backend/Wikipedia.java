package com.qiaoqiao.backend;

import com.qiaoqiao.backend.model.wikipedia.WikiResult;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface Wikipedia {
	final class WikiReqBody {
		String language;
		String keyword;

		public WikiReqBody(String language, String keyword) {
			this.language = language;
			this.keyword = keyword;
		}
	}

	@POST("knowledge/documents/wikipedia")
	Observable<WikiResult> getResult(@Body WikiReqBody body);


}
