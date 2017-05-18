package com.qiaoqiao.vision;


import android.content.Context;
import android.support.annotation.NonNull;

import com.qiaoqiao.camera.annotation.CameraScoped;
import com.qiaoqiao.vision.annotation.target.More;
import com.qiaoqiao.vision.annotation.target.Single;
import com.qiaoqiao.vision.ui.VisionListFragment;
import com.qiaoqiao.vision.ui.VisionMoreListFragment;

import dagger.Module;
import dagger.Provides;

@Module
public final class VisionModule {


	@Single
	@CameraScoped
	@Provides
	VisionContract.View provideVisionContractView(@NonNull Context cxt) {
		return VisionListFragment.newInstance(cxt);
	}


	@More
	@CameraScoped
	@Provides
	VisionContract.View provideMoreVisionContractView(@NonNull Context cxt) {
		return VisionMoreListFragment.newInstance(cxt);
	}


}
