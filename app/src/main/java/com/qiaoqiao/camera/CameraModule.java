package com.qiaoqiao.camera;

import android.support.annotation.NonNull;

import dagger.Module;
import dagger.Provides;

@Module
public final class CameraModule {
	private final @NonNull CameraContract.View mView;

	public CameraModule(@NonNull CameraContract.View view) {
		mView = view;
	}


	@Provides
	CameraContract.View provideHomeContractView() {
		return mView;
	}

}
