package com.qiaoqiao.home;

import android.support.annotation.NonNull;

import com.qiaoqiao.databinding.ActivityMainBinding;
import com.qiaoqiao.views.MainControl;

import dagger.Module;
import dagger.Provides;

@Module
public class HomeModule {
	private final ActivityMainBinding mBinding;

	public HomeModule(@NonNull ActivityMainBinding binding) {
		mBinding = binding;
	}

	@Provides
	public MainControl provideMainControl() {
		return mBinding.mainControl;
	}
}
