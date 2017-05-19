package com.qiaoqiao.repository.backend.model.wikipedia.geo;


import com.google.gson.annotations.SerializedName;

public final class Geosearch {
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

	public long getPageId() {
		return mPageId;
	}

	public void setPageId(long pageId) {
		mPageId = pageId;
	}

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
}
