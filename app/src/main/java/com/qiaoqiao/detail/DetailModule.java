package com.qiaoqiao.detail;


import android.support.annotation.NonNull;

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

	@Provides
	DetailContract.View provideDetailContractView() {
		return mView;
	}

	@Provides
	String provideKeyword() {
		return mKeyword;
	}
}
