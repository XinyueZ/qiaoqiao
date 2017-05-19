package com.qiaoqiao.repository.knowledge;


import android.support.annotation.NonNull;

import com.qiaoqiao.repository.backend.Google;
import com.qiaoqiao.repository.backend.Wikipedia;
import com.qiaoqiao.repository.backend.model.wikipedia.LangLink;
import com.qiaoqiao.repository.AbstractDsSource;
import com.qiaoqiao.repository.DsLoadedCallback;
import com.qiaoqiao.repository.annotation.RepositoryScope;
import com.qiaoqiao.app.Key;

import java.util.Locale;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

@RepositoryScope
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
