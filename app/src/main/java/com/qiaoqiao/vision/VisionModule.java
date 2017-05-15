package com.qiaoqiao.vision;


import android.support.annotation.NonNull;

import com.qiaoqiao.camera.annotation.CameraScoped;
import com.qiaoqiao.vision.annotation.More;
import com.qiaoqiao.vision.annotation.Single;

import dagger.Module;
import dagger.Provides;

@Module
public final class VisionModule {
	private final @NonNull VisionContract.View mVisionView;
	private final @NonNull VisionContract.View mMoreVisionView;

	public VisionModule(@NonNull VisionContract.View visionView, @NonNull VisionContract.View moreVisionView) {
		mVisionView = visionView;
		mMoreVisionView = moreVisionView;
	}


	@Single
	@CameraScoped
	@Provides
	VisionContract.View provideVisionContractView() {
		return mVisionView;
	}


	@More
	@CameraScoped
	@Provides
	VisionContract.View provideMoreVisionContractView() {
		return mMoreVisionView;
	}

}
