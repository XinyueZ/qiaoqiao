package com.qiaoqiao.home;

import android.support.annotation.NonNull;

import com.qiaoqiao.databinding.HomeBinding;

import dagger.Module;
import dagger.Provides;

@Module
public final class HomeModule {
	private final @NonNull HomeContract.View mView;
	private final @NonNull HomeBinding mBinding;

	public HomeModule(@NonNull HomeContract.View view, @NonNull HomeBinding binding) {
		mView = view;
		mBinding = binding;
	}


	@Provides
	HomeBinding provideHomeBinding() {
		return mBinding;
	}

	@Provides
	HomeContract.View provideHomeContractView() {
		return mView;
	}

}
