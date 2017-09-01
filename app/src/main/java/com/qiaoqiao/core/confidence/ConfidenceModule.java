package com.qiaoqiao.core.confidence;


import android.content.Context;
import android.support.annotation.NonNull;

import com.qiaoqiao.core.camera.annotation.CameraScoped;
import com.qiaoqiao.core.confidence.ui.ConfidenceDialogFragment;

import dagger.Module;
import dagger.Provides;

@Module
public final class ConfidenceModule {
	@Provides
	@CameraScoped
	ConfidenceContract.View provideConfidenceContractView(@NonNull Context cxt) {
		return ConfidenceDialogFragment.newInstance(cxt);
	}

}
