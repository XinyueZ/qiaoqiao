package com.qiaoqiao.core.camera.awareness.map;


import android.graphics.Bitmap;

import com.google.android.gms.location.places.Place;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

public final class PlaceWrapper implements ClusterItem{
	private final Place mPlace;
	private final Bitmap mBitmap;

	public PlaceWrapper(Place place, Bitmap bitmap) {
		mPlace = place;
		mBitmap = bitmap;
	}


	public Place getPlace() {
		return mPlace;
	}

	public Bitmap getBitmap() {
		return mBitmap;
	}

	@Override
	public LatLng getPosition() {
		return mPlace.getLatLng();
	}

	@Override
	public String getTitle() {
		return mPlace.getName().toString();
	}

	@Override
	public String getSnippet() {
		return null;
	}
}
