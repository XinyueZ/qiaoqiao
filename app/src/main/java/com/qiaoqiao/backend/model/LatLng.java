package com.qiaoqiao.backend.model;

import com.google.gson.annotations.SerializedName;

public final class LatLng {
	@SerializedName("latitude") private double latitude;
	@SerializedName("longitude") private double longitude;

	public LatLng(double latitude, double longitude) {
		this.latitude = latitude;
		this.longitude = longitude;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
}
