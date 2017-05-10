package com.qiaoqiao.ds.wikipedia;


import android.support.annotation.NonNull;

import com.qiaoqiao.backend.Google;
import com.qiaoqiao.backend.Wikipedia;
import com.qiaoqiao.backend.model.translate.Data;
import com.qiaoqiao.backend.model.translate.TranslateTextResponseTranslation;
import com.qiaoqiao.ds.AbstractDsSource;
import com.qiaoqiao.keymanager.Key;

import java.util.Locale;

import javax.inject.Singleton;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

@Singleton
public final class DsWikipediaRemoteSource extends AbstractDsSource {
	private @NonNull  final Key mKey;

	public DsWikipediaRemoteSource(@NonNull Key key, @NonNull Google google, @NonNull Wikipedia wikipedia) {
		super(google, wikipedia);
		mKey = key;
	}

	@Override
	public void onTranslate(@NonNull String q, @NonNull LoadedCallback callback) {
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
	public void onKnowledgeQuery(@NonNull String keyword, @NonNull LoadedCallback callback) {
		onTranslate(keyword, new AbstractDsSource.LoadedCallback() {
			@Override
			public void onTranslateData(@NonNull Data translateData) {
				super.onTranslateData(translateData);
				final TranslateTextResponseTranslation[] translations = translateData.getTranslations();
				final TranslateTextResponseTranslation translation = translations[0];
				if (translation == null) {
					return;
				}
				getWikipedia().getResult1("query", "json", "extracts|pageimages", "original|name", true, true, 1, "titles", translation.getTranslatedText())
				              .subscribeOn(Schedulers.io())
				              .observeOn(AndroidSchedulers.mainThread())
				              .subscribe(result -> {
					              if (result.getQuery()
					                        .getPages()
					                        .getList()
					                        .size() > 0) {
						              callback.onKnowledgeResponse(result);
					              } else {
						              getWikipedia().getResult2(
								              "https://en.wikipedia.org/w/api.php?format=json&action=query&prop=extracts|pageimages&piprop=original|name&explaintext=&exintro=&exlimit=1&redirects" +
										              "=titles&titles=" + keyword)
						                            .subscribeOn(Schedulers.io())
						                            .observeOn(AndroidSchedulers.mainThread())
						                            .subscribe(result1 -> callback.onKnowledgeResponse(result1));
					              }
				              });
			}
		});


	}

}
