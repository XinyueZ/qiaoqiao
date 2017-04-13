package com.qiaoqiao.backend.model.request;


import com.google.gson.annotations.SerializedName;
import com.qiaoqiao.backend.model.LatLng;

public final class LatLongRect {
	@SerializedName("minLatLng") private LatLng minLatLng;
	@SerializedName("maxLatLng") private LatLng maxLatLng;

	public LatLongRect(LatLng minLatLng, LatLng maxLatLng) {
		this.minLatLng = minLatLng;
		this.maxLatLng = maxLatLng;
	}

	public LatLng getMinLatLng() {
		return minLatLng;
	}

	public void setMinLatLng(LatLng minLatLng) {
		this.minLatLng = minLatLng;
	}

	public LatLng getMaxLatLng() {
		return maxLatLng;
	}

	public void setMaxLatLng(LatLng maxLatLng) {
		this.maxLatLng = maxLatLng;
	}
}
