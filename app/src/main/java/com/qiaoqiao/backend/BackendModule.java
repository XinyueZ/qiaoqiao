package com.qiaoqiao.backend;

import android.content.Context;
import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.qiaoqiao.backend.model.wikipedia.Page;
import com.qiaoqiao.backend.model.wikipedia.Pages;
import com.qiaoqiao.keymanager.Key;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public final class BackendModule {

	private @NonNull final String mLanguage;
	static final Gson GSON = new GsonBuilder().registerTypeAdapter(Pages.class, new PageAdapter())
	                                          .create();

	public BackendModule(@NonNull String language) {
		mLanguage = language;
	}

	@Provides
	Google provideGoogle(@NonNull Context cxt, @NonNull Key key) {
		return new Google(cxt, key);
	}

	@Provides
	Wikipedia provideWikipedia() {

		Retrofit r = new Retrofit.Builder().baseUrl(String.format("https://%s.wikipedia.org/", mLanguage))
		                                   .addConverterFactory(GsonConverterFactory.create(GSON))
		                                   .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
		                                   .build();
		return r.create(Wikipedia.class);
	}

}


final class PageAdapter implements JsonDeserializer<Pages> {
	public Pages deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
		List<Page> pages = new ArrayList<>();
		final Set<Map.Entry<String, JsonElement>> entrySet = json.getAsJsonObject()
		                                                         .entrySet();
		for (Map.Entry<String, JsonElement> entry : entrySet) {
			Page page = BackendModule.GSON.fromJson(entry.getValue(), Page.class);
			pages.add(page);
		}
		return new Pages(pages);
	}
}
