package com.qiaoqiao.repository.backend.model.wikipedia.geo;


import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceLikelihood;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.annotations.SerializedName;
import com.google.maps.android.clustering.ClusterItem;

public final class Geosearch implements ClusterItem,
                                        PlaceLikelihood {
	@SerializedName("pageid") private long mPageId;
	@SerializedName("title") private String mTitle;
	@SerializedName("lat") private double mLatitude;
	@SerializedName("lon") private double mLongitude;

	public Geosearch(long pageId, String title, double latitude, double longitude) {
		mPageId = pageId;
		mTitle = title;
		mLatitude = latitude;
		mLongitude = longitude;
	}

	@Override
	public LatLng getPosition() {
		return new LatLng(getLatitude(), getLongitude());
	}

	@Override
	public String getSnippet() {
		return mPageId + "";
	}

	public long getPageId() {
		return mPageId;
	}

	public void setPageId(long pageId) {
		mPageId = pageId;
	}

	@Override
	public String getTitle() {
		return mTitle;
	}

	public void setTitle(String title) {
		mTitle = title;
	}

	public double getLatitude() {
		return mLatitude;
	}

	public void setLatitude(double latitude) {
		mLatitude = latitude;
	}

	public double getLongitude() {
		return mLongitude;
	}

	public void setLongitude(double longitude) {
		mLongitude = longitude;
	}

	@Override
	public float getLikelihood() {
		return 0;
	}

	@Override
	public Place getPlace() {
		return null;
	}

	@Override
	public PlaceLikelihood freeze() {
		return this;
	}

	@Override
	public boolean isDataValid() {
		return true;
	}
}
