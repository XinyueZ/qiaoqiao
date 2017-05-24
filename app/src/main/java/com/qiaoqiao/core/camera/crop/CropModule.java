package com.qiaoqiao.core.camera.crop;


import android.content.Context;
import android.support.annotation.NonNull;

import com.qiaoqiao.core.camera.annotation.CameraScoped;
import com.qiaoqiao.core.camera.crop.ui.CropFragment;

import dagger.Module;
import dagger.Provides;

@Module
public final class CropModule {


	@Provides
	@CameraScoped
	CropContract.View provideCropContractView(@NonNull Context cxt) {
		return CropFragment.newInstance(cxt);
	}


}
