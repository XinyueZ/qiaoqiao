package com.qiaoqiao.ds.wikipedia;


import android.support.annotation.NonNull;

import com.qiaoqiao.backend.WikipediaAPIs;
import com.qiaoqiao.backend.model.wikipedia.WikiResult;
import com.qiaoqiao.ds.AbstractDsSource;

import javax.inject.Singleton;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

@Singleton
public final class DsWikipediaRemoteSource extends AbstractDsSource {

	public DsWikipediaRemoteSource(@NonNull WikipediaAPIs wikipediaAPIs) {
		super(wikipediaAPIs);
	}

	@Override
	public void onKnowledgeQuery(@NonNull String keyword, @NonNull LoadedCallback callback) {
		getWikipediaAPIs().getResult("query", "json", "extracts", true, true, 1, "titles", keyword)
		                  .subscribeOn(Schedulers.io())
		                  .observeOn(AndroidSchedulers.mainThread())
		                  .subscribe(new Consumer<WikiResult>() {
			                  @Override
			                  public void accept(@NonNull WikiResult result) {
				                  callback.onKnowledgeResponse(result);
			                  }
		                  });
	}

}
