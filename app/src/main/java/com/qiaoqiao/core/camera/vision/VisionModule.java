package com.qiaoqiao.core.camera.vision;


import android.content.Context;
import android.support.annotation.NonNull;

import com.qiaoqiao.app.Key;
import com.qiaoqiao.core.camera.annotation.CameraScoped;
import com.qiaoqiao.core.camera.vision.ui.VisionListFragment;

import dagger.Module;
import dagger.Provides;

@Module
public final class VisionModule {


	@CameraScoped
	@Provides
	VisionContract.View provideVisionContractView(@NonNull Context cxt,  @NonNull Key key) {
		return VisionListFragment.newInstance(cxt, key);
	}


}
