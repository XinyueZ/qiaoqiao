package com.qiaoqiao.camera;

import android.content.Context;
import android.support.annotation.NonNull;

import com.qiaoqiao.app.App;
import com.qiaoqiao.camera.annotation.CameraScoped;

import dagger.Module;
import dagger.Provides;

@Module
public final class CameraModule {
	private final @NonNull CameraContract.View mView;
	private final @NonNull App mApp;

	public CameraModule(@NonNull App app, @NonNull CameraContract.View view) {
		mApp = app;
		mView = view;
	}


	@CameraScoped
	@Provides
	CameraContract.View provideHomeContractView() {
		return mView;
	}

	@CameraScoped
	@Provides
	Context provideApp() {
		return mApp;
	}

}
