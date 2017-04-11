package com.qiaoqiao.home;

import android.support.annotation.NonNull;

import com.qiaoqiao.databinding.HomeBinding;

import dagger.Module;
import dagger.Provides;

@Module
public class HomeModule {
	private final HomeBinding mBinding;

	public HomeModule(@NonNull HomeBinding binding) {
		mBinding = binding;
	}


	@Provides
	public HomeBinding  provideHomeBinding() {
		return mBinding;
	}
}
