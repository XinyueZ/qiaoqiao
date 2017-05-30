package com.qiaoqiao.core.camera.awareness;


import android.content.Context;
import android.support.annotation.NonNull;

import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationSettingsRequest;
import com.qiaoqiao.core.camera.annotation.CameraScoped;
import com.qiaoqiao.core.camera.awareness.ui.SnapshotPlacesFragment;

import dagger.Module;
import dagger.Provides;

@Module
public final class AwarenessModule {


	@Provides
	@CameraScoped
	AwarenessContract.View provideAwarenessPlacesContractView(@NonNull Context cxt) {
		return SnapshotPlacesFragment.newInstance(cxt);
	}



	@Provides
	@CameraScoped
	LocationSettingsRequest.Builder provideLocationSettingsRequestBuilder() {
		return new LocationSettingsRequest.Builder().addLocationRequest(LocationRequest.create())
		                                            .setAlwaysShow(true)
		                                            .setNeedBle(true);
	}
}
