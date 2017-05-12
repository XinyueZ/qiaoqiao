package com.qiaoqiao.ds.knowledge;


import android.support.annotation.NonNull;

import com.qiaoqiao.backend.Google;
import com.qiaoqiao.backend.Wikipedia;
import com.qiaoqiao.backend.model.translate.Data;
import com.qiaoqiao.backend.model.translate.TranslateTextResponseTranslation;
import com.qiaoqiao.backend.model.wikipedia.LangLink;
import com.qiaoqiao.backend.model.wikipedia.WikiResult;
import com.qiaoqiao.ds.AbstractDsSource;
import com.qiaoqiao.ds.DsLoadedCallback;
import com.qiaoqiao.keymanager.Key;

import java.util.Locale;

import javax.inject.Singleton;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

@Singleton
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
	public void onKnowledgeQuery(@NonNull LangLink langLink, @NonNull DsLoadedCallback callback) {
		getWikipedia().getResult(wikiQuery(langLink.getLanguage(), langLink.getQuery()))
		              .subscribeOn(Schedulers.io())
		              .observeOn(AndroidSchedulers.mainThread())
		              .subscribe(callback::onKnowledgeResponse);
	}

	@Override
	public void onKnowledgeQuery(@NonNull String keyword, @NonNull DsLoadedCallback callback) {
		onTranslate(keyword, new DsLoadedCallback() {
			@Override
			public void onTranslateData(@NonNull Data translateData) {
				super.onTranslateData(translateData);
				final TranslateTextResponseTranslation[] translations = translateData.getTranslations();
				final TranslateTextResponseTranslation translation = translations[0];
				if (translation == null) {
					return;
				}


				getWikipedia().getResult(wikiQuery(Locale.getDefault()
				                                         .getLanguage(), translation.getTranslatedText()))
				              .subscribeOn(Schedulers.io())
				              .observeOn(AndroidSchedulers.mainThread())
				              .subscribe((WikiResult result) -> {
					              if (result.getQuery()
					                        .getPages()
					                        .getList()
					                        .size() > 0) {
						              callback.onKnowledgeResponse(result);
					              } else {
						              getWikipedia().getResult(wikiQuery("en", keyword))
						                            .subscribeOn(Schedulers.io())
						                            .observeOn(AndroidSchedulers.mainThread())
						                            .subscribe(result1 -> {
							                            if (result1.getQuery()
							                                       .getPages()
							                                       .getList()
							                                       .size() > 0) {
								                            callback.onKnowledgeResponse(result1);
							                            }
						                            });
					              }
				              });
			}
		});


	}

	private @NonNull
	String wikiQuery(@NonNull String lang, @NonNull String keyword) {
		return wikiHost(lang) + wikiQuery(keyword);
	}

	private @NonNull
	String wikiHost(@NonNull String lang) {
		return String.format("https://%s.wikipedia.org", lang);
	}

	private @NonNull
	String wikiQuery(@NonNull String keyword) {
		return "/w/api.php?format=json&action=query&prop=extracts|pageimages|langlinks&llprop=autonym&lldir=descending&lllimit=500&piprop=original|name|thumbnail&exlimit=1&redirects=titles&titles="
				+ keyword;
	}
}
