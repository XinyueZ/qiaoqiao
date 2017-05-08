package com.qiaoqiao.history;


import android.support.annotation.NonNull;

import com.qiaoqiao.databinding.FragmentHistoryBinding;

import dagger.Module;
import dagger.Provides;

@Module
public final class HistoryModule {
	private final @NonNull HistoryContract.View mView;

	public HistoryModule(@NonNull HistoryContract.View view) {
		mView = view;
	}


	@Provides
	HistoryContract.View provideHistoryContractView() {
		return mView;
	}

}
