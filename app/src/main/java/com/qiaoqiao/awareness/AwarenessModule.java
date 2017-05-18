package com.qiaoqiao.awareness;


import android.support.annotation.NonNull;

import com.qiaoqiao.camera.annotation.CameraScoped;

import dagger.Module;
import dagger.Provides;

@Module
public final class AwarenessModule {
	private final @NonNull AwarenessContract.View mView;

	public AwarenessModule(@NonNull AwarenessContract.View view) {
		mView = view;
	}

	@CameraScoped
	@Provides
	AwarenessContract.View provideAwarenessContractView() {
		return mView;
	}
}
