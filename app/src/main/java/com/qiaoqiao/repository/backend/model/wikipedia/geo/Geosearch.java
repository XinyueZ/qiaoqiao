package com.qiaoqiao.repository.backend.model.wikipedia.geo;


import com.google.android.gms.maps.model.LatLng;
import com.google.gson.annotations.SerializedName;
import com.google.maps.android.clustering.ClusterItem;

public final class Geosearch implements ClusterItem {
	@SerializedName("pageid") private int mPageId;
	@SerializedName("title") private String mTitle;
	@SerializedName("lat") private double mLatitude;
	@SerializedName("lon") private double mLongitude;

	public Geosearch(int pageId, String title, double latitude, double longitude) {
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
		return null;
	}

	@Override
	public String getTitle() {
		return mTitle;
	}

	public int getPageId() {
		return mPageId;
	}

	public void setPageId(int pageId) {
		mPageId = pageId;
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
}
