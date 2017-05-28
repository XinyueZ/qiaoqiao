package com.qiaoqiao.core.camera.awareness;


import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;

import com.google.android.gms.awareness.Awareness;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.places.PlaceLikelihood;
import com.google.android.gms.location.places.PlacePhotoMetadata;
import com.google.android.gms.location.places.PlacePhotoMetadataBuffer;
import com.google.android.gms.location.places.PlacePhotoMetadataResult;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;
import com.qiaoqiao.core.camera.awareness.map.PlaceWrapper;
import com.qiaoqiao.core.camera.ui.CameraActivity;
import com.qiaoqiao.repository.DsLoadedCallback;
import com.qiaoqiao.repository.DsRepository;
import com.qiaoqiao.repository.backend.model.wikipedia.geo.GeoResult;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public final class AwarenessPresenter implements AwarenessContract.Presenter,
                                                 GoogleApiClient.OnConnectionFailedListener,
                                                 ResultCallback<LocationSettingsResult> {
	private final @NonNull AwarenessContract.View mView;
	private final @NonNull GoogleApiClient.Builder mGoogleApiClientBuilder;
	private @Nullable GoogleApiClient mGoogleApiClient;
	private @NonNull WeakReference<CameraActivity> mCameraActivityWeakReference;
	private @NonNull LocationSettingsRequest.Builder mLocationSettingsRequestBuilder;
	private @NonNull DsRepository mDsRepository;

	@Inject
	AwarenessPresenter(@NonNull CameraActivity cameraActivity,
	                   @NonNull AwarenessContract.View view,
	                   @NonNull GoogleApiClient.Builder googleApiClientBuilder,
	                   @NonNull LocationSettingsRequest.Builder locationSettingsRequestBuilder,
	                   @NonNull DsRepository dsRepository) {
		mView = view;
		mGoogleApiClientBuilder = googleApiClientBuilder;
		mCameraActivityWeakReference = new WeakReference<>(cameraActivity);
		mLocationSettingsRequestBuilder = locationSettingsRequestBuilder;
		mDsRepository = dsRepository;
	}

	@Inject
	void onInjected() {
		mView.setPresenter(this);
	}

	@Override
	public void begin() {
		if (mGoogleApiClient == null && mCameraActivityWeakReference.get() != null) {
			mGoogleApiClient = mGoogleApiClientBuilder.enableAutoManage(mCameraActivityWeakReference.get(), this)
			                                          .build();
		}
	}

	@Override
	public void end() {
		if (mGoogleApiClient != null && mCameraActivityWeakReference.get() != null) {
			mGoogleApiClient.stopAutoManage(mCameraActivityWeakReference.get());

		}
		if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
			mGoogleApiClient.disconnect();
		}
		mGoogleApiClient = null;
	}

	@Override
	public void settingLocating() {
		PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, mLocationSettingsRequestBuilder.build());
		result.setResultCallback(this);
	}

	@Override
	public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
		mView.onPlayServiceConnectionFailed();
	}

	@Override
	public void locating(@NonNull Context cxt) {
		if (ActivityCompat.checkSelfPermission(cxt, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
			return;
		}
		if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
			Awareness.SnapshotApi.getLocation(mGoogleApiClient)
			                     .setResultCallback(locationResult -> {
				                     if (!locationResult.getStatus()
				                                        .isSuccess()) {
					                     mView.onLocatingError();
					                     return;
				                     }
				                     Location location = locationResult.getLocation();
				                     mView.onLocated(new LatLng(location.getLatitude(), location.getLongitude()));
			                     });
		}
	}

	@Override
	public void onResult(@NonNull LocationSettingsResult locationSettingsResult) {
		final Status status = locationSettingsResult.getStatus();
		switch (status.getStatusCode()) {
			case LocationSettingsStatusCodes.SUCCESS:
				mView.locating();
				break;
			case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
				mView.solveSettingLocatingDialogProblem(status);
				break;
			case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
				mView.onLocatingError();
				break;
		}
	}

	@Override
	public void searchAndSearch(@NonNull LatLng latLng) {
		mDsRepository.onGeosearchQuery(latLng, new DsLoadedCallback() {
			@Override
			public void onGeosearchResponse(GeoResult result) {
				super.onGeosearchResponse(result);
				List<ClusterItem> clusterItemList = new ArrayList<>();
				clusterItemList.addAll(Arrays.asList(result.getQuery()
				                                           .getGeosearches()));
				getPlaces(clusterItemList);
			}
		});
	}


	private void getPlaces(@NonNull List<ClusterItem> outputList) {
		if (mCameraActivityWeakReference.get() == null || ActivityCompat.checkSelfPermission(mCameraActivityWeakReference.get(),
		                                                                                     Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
			return;
		}
		if (mGoogleApiClient == null || !mGoogleApiClient.isConnected() || mCameraActivityWeakReference.get() == null) {
			return;
		}
		Awareness.SnapshotApi.getPlaces(mGoogleApiClient)
		                     .setResultCallback(placesResult -> {
			                     if (!placesResult.getStatus()
			                                      .isSuccess()) {
				                     return;
			                     }
			                     showPlaces(placesResult.getPlaceLikelihoods(), outputList);
		                     });
	}

	private void showPlaces(@NonNull List<PlaceLikelihood> placeLikelihoodList, @NonNull List<ClusterItem> outputList) {
		if(mGoogleApiClient == null) return;
		Observable.just(placeLikelihoodList)
		          .subscribeOn(Schedulers.newThread())
		          .map(likelihoodList -> {
			          createPlaceWrappers(placeLikelihoodList, outputList);
			          return outputList;
		          })
		          .observeOn(AndroidSchedulers.mainThread())
		          .subscribe(mView::showAllGeoAndPlaces);
	}

	private void createPlaceWrappers(@NonNull List<PlaceLikelihood> likelihoodList, @NonNull List<ClusterItem> outputList) {
		if(mGoogleApiClient == null) return;
		for (PlaceLikelihood placeLikelihood : likelihoodList) {
			PlacePhotoMetadataResult result = Places.GeoDataApi.getPlacePhotos(mGoogleApiClient,
			                                                                   placeLikelihood.getPlace()
			                                                                                  .getId())
			                                                   .await();
			if (result.getStatus()
			          .isSuccess()) {
				PlacePhotoMetadataBuffer photoMetadataBuffer = result.getPhotoMetadata();
				if (photoMetadataBuffer.getCount() > 0) {
					PlacePhotoMetadata photo = photoMetadataBuffer.get(0);
					Bitmap image = photo.getScaledPhoto(mGoogleApiClient, photo.getMaxWidth(), photo.getMaxHeight())
					                    .await()
					                    .getBitmap();
					outputList.add(new PlaceWrapper(placeLikelihood.getPlace(), image));
				}
				photoMetadataBuffer.release();
			}
		}
	}

}
