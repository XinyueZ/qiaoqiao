package com.qiaoqiao.backend.model.wikipedia;

import com.google.gson.annotations.SerializedName;

public final class Thumbnail {
	@SerializedName("original")
	private String mOriginal;


	public Thumbnail(String original) {
		this.mOriginal = original;
	}

	public String getOriginal() {
		return mOriginal;
	}

	public void setOriginal(String original) {
		this.mOriginal = original;
	}
}
