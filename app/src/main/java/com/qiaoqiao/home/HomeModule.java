package com.qiaoqiao.home;

import android.support.annotation.NonNull;

import dagger.Module;
import dagger.Provides;

@Module
public final class HomeModule {
	private final @NonNull HomeContract.View mView;

	public HomeModule(@NonNull HomeContract.View view) {
		mView = view;
	}


	@Provides
	HomeContract.View provideHomeContractView() {
		return mView;
	}

}
