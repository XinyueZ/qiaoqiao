package com.qiaoqiao.vision.model;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.google.android.gms.maps.model.LatLng;
import com.google.api.services.vision.v1.model.EntityAnnotation;
import com.google.api.services.vision.v1.model.LocationInfo;

import java.util.List;

public final class VisionEntityLocation {
	private final @NonNull VisionEntity mVisionEntity;

	VisionEntityLocation(@NonNull VisionEntity visionEntity) {
		mVisionEntity = visionEntity;
	}


	public @Nullable
	LatLng toLatLng() {
		if (TextUtils.equals(mVisionEntity.getReadableName(), "LANDMARK_DETECTION")) {
			EntityAnnotation landmarkEntity = (EntityAnnotation) mVisionEntity.getVision();
			final List<LocationInfo> locations = landmarkEntity.getLocations();
			if (locations != null && locations.size() > 0) {
				LocationInfo locationInfo = locations.get(0);
				return new LatLng(locationInfo.getLatLng()
				                              .getLatitude(),
				                  locationInfo.getLatLng()
				                              .getLongitude());
			}

		}
		return null;
	}

	@Override
	@Nullable
	public String toString() {
		if (toLatLng() == null) {
			return "No location";
		}
		return toLatLng().toString();
	}
}
