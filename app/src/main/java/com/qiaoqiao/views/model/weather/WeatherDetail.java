package com.qiaoqiao.views.model.weather;


import com.google.gson.annotations.SerializedName;

public final class WeatherDetail {
	@SerializedName("id")
	private long   mId;
	@SerializedName("main")
	private String mName;
	@SerializedName("description")
	private String mDescription;
	@SerializedName("icon")
	private String mIcon;


	public long getId() {
		return mId;
	}

	public String getName() {
		return mName;
	}

	public String getDescription() {
		return mDescription;
	}

	public String getIcon() {
		return mIcon;
	}
}
