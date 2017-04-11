package com.qiaoqiao.home;

import android.support.annotation.NonNull;

import com.qiaoqiao.databinding.HomeBinding;

import dagger.Module;
import dagger.Provides;

@Module
public class HomeModule {
	private final @NonNull HomeContract.View mView;
	private final @NonNull HomeBinding mBinding;

	public HomeModule(@NonNull HomeContract.View view, @NonNull HomeBinding binding) {
		mView = view;
		mBinding = binding;
	}


	@Provides
	public HomeBinding provideHomeBinding() {
		return mBinding;
	}

	@Provides
	public HomeContract.View provideHomeContractView() {
		return mView;
	}

}
