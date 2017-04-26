package com.qiaoqiao.backend;

import android.content.Context;
import android.support.annotation.NonNull;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.qiaoqiao.keymanager.Key;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


@Module
public final class BackendModule {

	private @NonNull String mLanguage;

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
		                                   .addConverterFactory(GsonConverterFactory.create())
		                                   .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
		                                   .build();
		return r.create(Wikipedia.class);
	}

}
