package com.qiaoqiao.core.camera.awareness;


import android.content.Context;
import android.support.annotation.NonNull;

import com.qiaoqiao.core.camera.awareness.ui.SnapshotPlacesFragment;
import com.qiaoqiao.core.camera.annotation.CameraScoped;

import dagger.Module;
import dagger.Provides;

@Module
public final class AwarenessModule {


	@Provides
	@CameraScoped
	AwarenessContract.View  provideAwarenessPlacesContractView(@NonNull Context cxt) {
		return SnapshotPlacesFragment.newInstance(cxt);
	}
}
