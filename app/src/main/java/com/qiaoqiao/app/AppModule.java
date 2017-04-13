package com.qiaoqiao.app;


import android.content.Context;
import android.support.annotation.NonNull;

import dagger.Module;
import dagger.Provides;

@Module
public final class AppModule {
	private @NonNull App mApp;


	AppModule(@NonNull App app) {
		mApp = app;
	}

	@Provides
	Context provideApp() {
		return mApp;
	}
}
