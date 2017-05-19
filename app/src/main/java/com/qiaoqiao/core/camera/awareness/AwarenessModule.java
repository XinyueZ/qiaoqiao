package com.qiaoqiao.core.camera.awareness;


import android.content.Context;
import android.support.annotation.NonNull;

import com.google.android.gms.awareness.Awareness;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.places.Places;
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
	GoogleApiClient.Builder provideGoogleApiClientBuilder(@NonNull Context cxt) {
		return new GoogleApiClient.Builder(cxt).addApi(Awareness.API)
		                                       .addApi(Places.GEO_DATA_API)
		                                       .addApi(LocationServices.API);
	}

	@Provides
	@CameraScoped
	LocationSettingsRequest.Builder provideLocationSettingsRequestBuilder() {
		return new LocationSettingsRequest.Builder().addLocationRequest(LocationRequest.create()).setAlwaysShow(true)
		                                            .setNeedBle(true);
	}
}
