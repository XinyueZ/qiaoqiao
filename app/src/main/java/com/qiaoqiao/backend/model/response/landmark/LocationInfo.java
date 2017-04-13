package com.qiaoqiao.backend.model.response.landmark;


import com.google.gson.annotations.SerializedName;
import com.qiaoqiao.backend.model.LatLng;

public final class LocationInfo {
	@SerializedName("latLng") private LatLng latLng;

	public LocationInfo(LatLng latLng) {
		this.latLng = latLng;
	}


	public LatLng getLatLng() {
		return latLng;
	}

	public void setLatLng(LatLng latLng) {
		this.latLng = latLng;
	}
}
