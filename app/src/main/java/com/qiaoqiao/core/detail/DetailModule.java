package com.qiaoqiao.core.detail;


import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;

import com.qiaoqiao.R;
import com.qiaoqiao.core.detail.annotation.DetailScoped;

import dagger.Module;
import dagger.Provides;

@Module
public final class DetailModule {
	private final @NonNull String mKeyword;
	private final @NonNull FragmentManager mFragmentManager;

	public DetailModule(@NonNull FragmentManager fragmentManager, @NonNull String keyword) {
		mFragmentManager = fragmentManager;
		mKeyword = keyword;
	}

	@DetailScoped
	@Provides
	DetailContract.View provideDetailContractView() {
		return (DetailContract.View) mFragmentManager.findFragmentById(R.id.detail_fg);
	}

	@DetailScoped
	@Provides
	String provideKeyword() {
		return mKeyword;
	}
}
