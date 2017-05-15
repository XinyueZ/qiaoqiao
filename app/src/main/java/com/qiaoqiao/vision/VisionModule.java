package com.qiaoqiao.vision;


import android.support.annotation.NonNull;

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
	@Provides
	VisionContract.View provideVisionContractView() {
		return mVisionView;
	}


	@More
	@Provides
	VisionContract.View provideMoreVisionContractView() {
		return mMoreVisionView;
	}

}
