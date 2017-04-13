package com.qiaoqiao.backend.model.request;


import com.google.gson.annotations.SerializedName;

public final class ImageSource {
	@SerializedName("gcsImageUri") private String gcsImageUri;
	@SerializedName("imageUri") private String imageUri;

	public ImageSource(String gcsImageUri, String imageUri) {
		this.gcsImageUri = gcsImageUri;
		this.imageUri = imageUri;
	}

	public String getGcsImageUri() {
		return gcsImageUri;
	}

	public void setGcsImageUri(String gcsImageUri) {
		this.gcsImageUri = gcsImageUri;
	}

	public String getImageUri() {
		return imageUri;
	}

	public void setImageUri(String imageUri) {
		this.imageUri = imageUri;
	}
}
