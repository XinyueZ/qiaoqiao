package com.qiaoqiao.vision;


import android.support.annotation.NonNull;

import dagger.Module;
import dagger.Provides;

@Module
public final class VisionModule {
	private final @NonNull VisionContract.View mView;

	public VisionModule(@NonNull VisionContract.View view) {
		mView = view;
	}


	@Provides
	VisionContract.View provideVisionContractView() {
		return mView;
	}

}
