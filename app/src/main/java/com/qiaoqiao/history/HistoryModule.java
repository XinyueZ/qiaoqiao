package com.qiaoqiao.history;


import android.support.annotation.NonNull;

import com.qiaoqiao.camera.annotation.CameraScoped;

import dagger.Module;
import dagger.Provides;

@Module
public final class HistoryModule {
	private final @NonNull HistoryContract.View mView;

	public HistoryModule(@NonNull HistoryContract.View view) {
		mView = view;
	}

	@CameraScoped
	@Provides
	HistoryContract.View provideHistoryContractView() {
		return mView;
	}

}
