package com.qiaoqiao.core.camera;

import android.support.annotation.NonNull;

import com.qiaoqiao.core.camera.annotation.CameraScoped;
import com.qiaoqiao.core.camera.crop.CropCallback;
import com.qiaoqiao.core.camera.ui.CameraActivity;

import dagger.Module;
import dagger.Provides;

@Module
public final class CameraModule {
	private final @NonNull CameraContract.View mView;

	public CameraModule(@NonNull CameraContract.View view) {

		mView = view;
	}


	@CameraScoped
	@Provides
	CameraContract.View provideHomeContractView() {
		return mView;
	}

	@CameraScoped
	@Provides
	CameraActivity provideCameraActivity() {
		return (CameraActivity)mView;
	}

	@CameraScoped
	@Provides
	CropCallback provideCropCallback() {
		return (CropCallback)mView;
	}
}
