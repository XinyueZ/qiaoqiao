package com.qiaoqiao.core.camera.history;


import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;

import com.qiaoqiao.R;
import com.qiaoqiao.core.camera.annotation.CameraScoped;

import dagger.Module;
import dagger.Provides;

@Module
public final class HistoryModule {
	private final @NonNull FragmentManager mFragmentManager;

	public HistoryModule(@NonNull FragmentManager fragmentManager) {
		mFragmentManager = fragmentManager;
	}


	@Provides
	@CameraScoped
	HistoryContract.View2 provideHistoryContractView2() {
		return (HistoryContract.View2) mFragmentManager.findFragmentById(R.id.stackview_history_fg);
	}

}
