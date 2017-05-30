package com.qiaoqiao.core.camera;

import android.content.Context;
import android.support.annotation.NonNull;

import com.google.android.gms.appinvite.AppInvite;
import com.google.android.gms.awareness.Awareness;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Places;
import com.qiaoqiao.core.camera.annotation.CameraScoped;
import com.qiaoqiao.core.camera.crop.CropCallback;
import com.qiaoqiao.core.camera.history.HistoryCallback;

import dagger.Module;
import dagger.Provides;

@Module
public final class CameraModule {
	private final @NonNull CameraContract.View mView;

	public CameraModule(@NonNull CameraContract.View view) {

		mView = view;
	}


	@CameraScoped
	@Provides
	CameraContract.View provideHomeContractView() {
		return mView;
	}


	@CameraScoped
	@Provides
	CropCallback provideCropCallback() {
		return (CropCallback)mView;
	}

	@CameraScoped
	@Provides
	HistoryCallback provideHistoryCallback() {
		return (HistoryCallback)mView;
	}

	@Provides
	@CameraScoped
	GoogleApiClient.Builder provideGoogleApiClientBuilder(@NonNull Context cxt) {
		return new GoogleApiClient.Builder(cxt).addApi(Awareness.API)
		                                       .addApi(AppInvite.API)
		                                       .addApi(Places.GEO_DATA_API)
		                                       .addApi(LocationServices.API);
	}

}
