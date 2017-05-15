package com.qiaoqiao.ds.knowledge;


import android.net.Uri;
import android.support.annotation.NonNull;

import com.qiaoqiao.backend.Google;
import com.qiaoqiao.backend.Wikipedia;
import com.qiaoqiao.backend.model.translate.Data;
import com.qiaoqiao.backend.model.translate.TranslateTextResponseTranslation;
import com.qiaoqiao.backend.model.wikipedia.LangLink;
import com.qiaoqiao.backend.model.wikipedia.Page;
import com.qiaoqiao.backend.model.wikipedia.WikiResult;
import com.qiaoqiao.ds.AbstractDsSource;
import com.qiaoqiao.ds.DsLoadedCallback;
import com.qiaoqiao.ds.annotation.DsScope;
import com.qiaoqiao.keymanager.Key;
import com.qiaoqiao.vision.model.VisionEntity;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;

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
	public void onKnowledgeQuery(@NonNull LangLink langLink, @NonNull DsLoadedCallback callback) {
		getWikipedia().getResult(wikiQuery(langLink.getLanguage(), langLink.getQuery()))
		              .subscribeOn(Schedulers.io())
		              .observeOn(AndroidSchedulers.mainThread())
		              .subscribe(callback::onKnowledgeResponse);
	}

	@Override
	public void onKnowledgeQuery(@NonNull String keyword, @NonNull DsLoadedCallback callback) {
		//Local language on local language wikipedia.
		getWikipedia().getResult(wikiQuery(Locale.getDefault()
		                                         .getLanguage(), keyword))
		              .subscribeOn(Schedulers.io())
		              .observeOn(AndroidSchedulers.mainThread())
		              .subscribe(result1 -> {
			              if (result1.getQuery()
			                         .getPages()
			                         .getList()
			                         .size() > 0) {
				              callback.onKnowledgeResponse(result1);
			              } else {
				              //Translate to local language.
				              onTranslate(keyword, new DsLoadedCallback() {
					              @Override
					              public void onTranslateData(@NonNull Data translateData) {
						              super.onTranslateData(translateData);
						              final TranslateTextResponseTranslation[] translations = translateData.getTranslations();
						              final TranslateTextResponseTranslation translation = translations[0];
						              if (translation == null) {
							              return;
						              }

						              //Use local language to search something on wikipedia.
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
								                            //Only English wikipedia as fallback.
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
		              });
	}

	@Override
	public void onKnowledgeQuery(@NonNull List<VisionEntity> list) throws IOException {
		for (VisionEntity entity : list) {
			//Local language on local language wikipedia.
			Call<WikiResult> wiki = getWikipedia().getWiki(wikiImage(Locale.getDefault()
			                                                               .getLanguage(),
			                                                         entity.getDescription()
			                                                               .getDescriptionText()));
			List<Page> listWikiRes = wiki.execute()
			                             .body()
			                             .getQuery()
			                             .getPages()
			                             .getList();
			if (listWikiRes.size() > 0) {
				if (listWikiRes.get(0)
				               .getOriginal() != null) {
					entity.setImageUri(Uri.parse(listWikiRes.get(0)
					                                        .getOriginal()
					                                        .getSource()));
				} else {
					if (listWikiRes.get(0)
					               .getThumbnail() != null) {
						entity.setImageUri(Uri.parse(listWikiRes.get(0)
						                                        .getThumbnail()
						                                        .getSource()));
					}
				}
				continue;
			}
			//Translate to local language.
			final Call<com.qiaoqiao.backend.model.translate.Response> translator = getGoogle().getTranslateService()
			                                                                                  .getTranslator(entity.getDescription()
			                                                                                                       .getDescriptionText(),
			                                                                                                 Locale.getDefault()
			                                                                                                       .getLanguage(),
			                                                                                                 "text",
			                                                                                                 mKey.toString());
			final TranslateTextResponseTranslation[] translations = translator.execute()
			                                                                  .body()
			                                                                  .getData()
			                                                                  .getTranslations();
			final TranslateTextResponseTranslation translation = translations[0];
			if (translation == null) {
				return;
			}
			//Use local language to search something on wikipedia.
			wiki = getWikipedia().getWiki(wikiImage(Locale.getDefault()
			                                              .getLanguage(), translation.getTranslatedText()));
			listWikiRes = wiki.execute()
			                  .body()
			                  .getQuery()
			                  .getPages()
			                  .getList();
			if (listWikiRes.size() > 0) {
				if (listWikiRes.get(0)
				               .getOriginal() != null) {
					entity.setImageUri(Uri.parse(listWikiRes.get(0)
					                                        .getOriginal()
					                                        .getSource()));
				} else {
					if (listWikiRes.get(0)
					               .getThumbnail() != null) {
						entity.setImageUri(Uri.parse(listWikiRes.get(0)
						                                        .getThumbnail()
						                                        .getSource()));
					}
				}
			} else {
				//Only English wikipedia as fallback.
				wiki = getWikipedia().getWiki(wikiImage("en",
				                                        entity.getDescription()
				                                              .getDescriptionText()));
				listWikiRes = wiki.execute()
				                  .body()
				                  .getQuery()
				                  .getPages()
				                  .getList();
				if (listWikiRes.size() > 0) {
					if (listWikiRes.get(0)
					               .getOriginal() != null) {
						entity.setImageUri(Uri.parse(listWikiRes.get(0)
						                                        .getOriginal()
						                                        .getSource()));
					} else {
						if (listWikiRes.get(0)
						               .getThumbnail() != null) {
							entity.setImageUri(Uri.parse(listWikiRes.get(0)
							                                        .getThumbnail()
							                                        .getSource()));
						}
					}
				}
			}
		}
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

	private @NonNull
	String wikiImage(@NonNull String lang, @NonNull String keyword) {
		return wikiHost(lang) + wikiImage(keyword);
	}


	private @NonNull
	String wikiImage(@NonNull String keyword) {
		return "/w/api.php?format=json&action=query&prop=pageimages&piprop=original|thumbnail&pilimit=1&redirects=titles&titles=" + keyword;
	}
}
