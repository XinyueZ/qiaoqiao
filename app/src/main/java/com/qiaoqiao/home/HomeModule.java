package com.qiaoqiao.home;

import android.content.Context;
import android.support.annotation.NonNull;

import com.qiaoqiao.databinding.HomeBinding;

import dagger.Module;
import dagger.Provides;

@Module
public class HomeModule {
	private final @NonNull Context mContext;
	private final @NonNull HomeContract.View mView;
	private final @NonNull HomeBinding mBinding;

	public HomeModule(@NonNull Context cxt, @NonNull HomeContract.View view, @NonNull HomeBinding binding) {
		mContext = cxt;
		mView = view;
		mBinding = binding;
	}

	@Provides
	public Context provideContext() {
		return mContext;
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
