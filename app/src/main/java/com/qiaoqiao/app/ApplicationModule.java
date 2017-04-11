package com.qiaoqiao.app;

import android.support.annotation.NonNull;

import dagger.Module;
import dagger.Provides;

@Module
public final class ApplicationModule {

	private final App mApp;

	ApplicationModule(@NonNull App app) {
		mApp = app;
	}

	@Provides
	App provideApp() {
		return mApp;
	}
}