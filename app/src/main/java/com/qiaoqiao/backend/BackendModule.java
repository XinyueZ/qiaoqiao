package com.qiaoqiao.backend;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public final class BackendModule {

	@Provides
	Service provideService() {
		Retrofit r = new Retrofit.Builder().baseUrl("https://vision.googleapis.com/v1/")
		                                   .addConverterFactory(GsonConverterFactory.create())
		                                   .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
		                                   .build();
		return r.create(Service.class);
	}
}
