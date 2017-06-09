package com.qiaoqiao.repository.backend;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.qiaoqiao.R;
import com.qiaoqiao.app.Key;
import com.qiaoqiao.repository.annotation.RepositoryScope;
import com.qiaoqiao.repository.backend.model.wikipedia.Page;
import com.qiaoqiao.repository.backend.model.wikipedia.Pages;

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

	static final Gson GSON = new GsonBuilder().registerTypeAdapter(Pages.class, new PageAdapter())
	                                          .create();

	@RepositoryScope
	@Provides
	Google provideGoogle(@NonNull Context cxt, @NonNull Key key) {
		return new Google(cxt, key);
	}

	@RepositoryScope
	@Provides
	Wikipedia provideKnowledge(@NonNull Context cxt) {

		Retrofit r = new Retrofit.Builder().baseUrl(cxt.getString(R.string.base_url_knowledge))
		                                   .addConverterFactory(GsonConverterFactory.create(GSON))
		                                   .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
		                                   .build();
		return r.create(Wikipedia.class);
	}

	@RepositoryScope
	@Provides
	ImageProvider provideImageProvider(@NonNull Context cxt) {

		Retrofit r = new Retrofit.Builder().baseUrl(cxt.getString(R.string.base_url_zhihu))
		                                   .addConverterFactory(GsonConverterFactory.create(GSON))
		                                   .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
		                                   .build();
		return r.create(ImageProvider.class);
	}
}


final class PageAdapter implements JsonDeserializer<Pages> {
	public Pages deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
		List<Page> pages = new ArrayList<>();
		final Set<Map.Entry<String, JsonElement>> entrySet = json.getAsJsonObject()
		                                                         .entrySet();
		for (Map.Entry<String, JsonElement> entry : entrySet) {
			if (!TextUtils.equals(entry.getKey(), "-1")) {
				Page page = BackendModule.GSON.fromJson(entry.getValue(), Page.class);
				pages.add(page);
			}
		}
		return new Pages(pages);
	}
}
