package com.qiaoqiao.history;


import android.content.Context;
import android.support.annotation.NonNull;

import com.qiaoqiao.camera.annotation.CameraScoped;
import com.qiaoqiao.history.ui.HistoryFragment;

import dagger.Module;
import dagger.Provides;

@Module
public final class HistoryModule {


	@Provides
	@CameraScoped
	HistoryContract.View provideHistoryContractView(@NonNull Context cxt) {
		return HistoryFragment.newInstance(cxt);
	}

}
