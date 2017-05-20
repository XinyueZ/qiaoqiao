package com.qiaoqiao.core.camera.awareness.map;


import com.google.android.gms.location.places.Place;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

public final class PlaceWrapper implements ClusterItem{
	private final Place mPlace;
	private final BitmapDescriptor mBitmapDescriptor;

	public PlaceWrapper(Place place, BitmapDescriptor bitmapDescriptor) {
		mPlace = place;
		mBitmapDescriptor = bitmapDescriptor;
	}

	public Place getPlace() {
		return mPlace;
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

	public BitmapDescriptor getBitmapDescriptor() {
		return mBitmapDescriptor;
	}
}
