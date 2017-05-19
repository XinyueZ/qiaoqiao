package com.qiaoqiao.core.camera.history;


import android.content.Context;
import android.support.annotation.NonNull;

import com.qiaoqiao.core.camera.annotation.CameraScoped;
import com.qiaoqiao.core.camera.history.ui.HistoryFragment;

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
