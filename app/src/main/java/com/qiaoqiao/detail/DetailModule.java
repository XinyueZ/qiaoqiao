package com.qiaoqiao.detail;


import android.support.annotation.NonNull;

import com.qiaoqiao.detail.annotation.DetailScoped;

import dagger.Module;
import dagger.Provides;

@Module
public final class DetailModule {
	private final @NonNull DetailContract.View mView;
	private final @NonNull String mKeyword;

	public DetailModule(@NonNull DetailContract.View view, @NonNull String keyword) {
		mView = view;
		mKeyword = keyword;
	}

	@DetailScoped
	@Provides
	DetailContract.View provideDetailContractView() {
		return mView;
	}

	@DetailScoped
	@Provides
	String provideKeyword() {
		return mKeyword;
	}
}
