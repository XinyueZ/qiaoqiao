package com.qiaoqiao.ds.wikipedia;


import android.support.annotation.NonNull;

import com.qiaoqiao.backend.Wikipedia;
import com.qiaoqiao.ds.AbstractDsSource;

import javax.inject.Singleton;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

@Singleton
public final class DsWikipediaRemoteSource extends AbstractDsSource {

	public DsWikipediaRemoteSource(@NonNull Wikipedia wikipedia) {
		super(wikipedia);
	}

	@Override
	public void onKnowledgeQuery(@NonNull String keyword, @NonNull LoadedCallback callback) {
		getWikipedia().getResult("query", "json", "extracts", true, true, 1, "titles", keyword)
		              .subscribeOn(Schedulers.io())
		              .observeOn(AndroidSchedulers.mainThread())
		              .subscribe(result -> callback.onKnowledgeResponse(result));
	}

}
