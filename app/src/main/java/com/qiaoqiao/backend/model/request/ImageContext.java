package com.qiaoqiao.backend.model.request;


import com.google.gson.annotations.SerializedName;

public final class ImageContext {
	@SerializedName("latLongRect") private LatLongRect latLongRect;
	@SerializedName("languageHints") private String[] languageHints;

	public ImageContext(LatLongRect latLongRect, String... languageHints) {
		this.latLongRect = latLongRect;
		this.languageHints = languageHints;
	}

	public LatLongRect getLatLongRect() {
		return latLongRect;
	}

	public void setLatLongRect(LatLongRect latLongRect) {
		this.latLongRect = latLongRect;
	}

	public String[] getLanguageHints() {
		return languageHints;
	}

	public void setLanguageHints(String[] languageHints) {
		this.languageHints = languageHints;
	}
}
