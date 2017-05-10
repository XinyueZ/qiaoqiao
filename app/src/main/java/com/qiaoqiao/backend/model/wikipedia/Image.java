package com.qiaoqiao.backend.model.wikipedia;


import com.google.gson.annotations.SerializedName;

public final class Image {
	@SerializedName("source")
	private String mSource;
	@SerializedName("width")
	private int mWidth;
	@SerializedName("height")
	private int mHeight;


	public Image(String source, int width, int height) {
		this.mSource = source;
		this.mWidth = width;
		this.mHeight = height;
	}


	public String getSource() {
		return mSource;
	}

	public void setSource(String source) {
		this.mSource = source;
	}

	public int getWidth() {
		return mWidth;
	}

	public void setWidth(int width) {
		this.mWidth = width;
	}

	public int getHeight() {
		return mHeight;
	}

	public void setHeight(int height) {
		this.mHeight = height;
	}
}
