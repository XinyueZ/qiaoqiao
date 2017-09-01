package com.qiaoqiao.views.model.weather;

import com.google.gson.annotations.SerializedName;

public final class Temperature {
	@SerializedName("temp")
	private double mValue;

	public double getValue() {
		return mValue;
	}
}
