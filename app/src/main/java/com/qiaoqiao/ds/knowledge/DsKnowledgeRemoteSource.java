package com.qiaoqiao.ds.knowledge;


import android.support.annotation.NonNull;

import com.qiaoqiao.backend.Google;
import com.qiaoqiao.backend.Wikipedia;
import com.qiaoqiao.backend.model.wikipedia.LangLink;
import com.qiaoqiao.ds.AbstractDsSource;
import com.qiaoqiao.ds.DsLoadedCallback;
import com.qiaoqiao.ds.annotation.DsScope;
import com.qiaoqiao.keymanager.Key;

import java.util.Locale;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

@DsScope
public final class DsKnowledgeRemoteSource extends AbstractDsSource {
	private @NonNull  final Key mKey;

	public DsKnowledgeRemoteSource(@NonNull Key key, @NonNull Google google, @NonNull Wikipedia wikipedia) {
		super(google, wikipedia);
		mKey = key;
	}

	@Override
	public void onTranslate(@NonNull String q, @NonNull DsLoadedCallback callback) {
		getGoogle().getTranslateService()
		           .translate(q,
		                      Locale.getDefault()
		                            .getLanguage(),
		                      "text",
		                      mKey.toString())
		           .subscribeOn(Schedulers.io())
		           .observeOn(AndroidSchedulers.mainThread())
		           .subscribe(response -> callback.onTranslateData(response.getData()));
	}


	@Override
	public void onKnowledgeQuery(@NonNull String keyword, @NonNull DsLoadedCallback callback) {
		getWikipedia().getResult(new Wikipedia.WikiReqBody(Locale.getDefault()
		                                                         .getLanguage(), keyword))
		              .subscribeOn(Schedulers.io())
		              .observeOn(AndroidSchedulers.mainThread())
		              .subscribe(result1 -> {
			              try {
				              if (result1.getQuery()
				                         .getPages()
				                         .getList()
				                         .size() > 0) {
					              callback.onKnowledgeResponse(result1);
				              }
			              } catch (Exception e) {
				              callback.onException(e);
			              }
		              });
	}

	@Override
	public void onKnowledgeQuery(@NonNull LangLink langLink, @NonNull DsLoadedCallback callback) {
		getWikipedia().getResult(new Wikipedia.WikiReqBody(langLink.getLanguage(), langLink.getQuery()))
		              .subscribeOn(Schedulers.io())
		              .observeOn(AndroidSchedulers.mainThread())
		              .subscribe(result1 -> {
			              try {
				              if (result1.getQuery()
				                         .getPages()
				                         .getList()
				                         .size() > 0) {
					              callback.onKnowledgeResponse(result1);
				              }
			              } catch (Exception e) {
				              callback.onException(e);
			              }
		              });
	}
}
