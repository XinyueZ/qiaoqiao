package com.qiaoqiao.backend.model.request;

import com.google.gson.annotations.SerializedName;

public final class AnnotateImageRequest {
	@SerializedName("image") private Image image;
	@SerializedName("features") private Feature[] features;
	@SerializedName("imageContext") private ImageContext imageContext;

	public AnnotateImageRequest(Image image, ImageContext imageContext, Feature... features) {
		this.image = image;
		this.features = features;
		this.imageContext = imageContext;
	}


	public Image getImage() {
		return image;
	}

	public void setImage(Image image) {
		this.image = image;
	}

	public Feature[] getFeatures() {
		return features;
	}

	public void setFeatures(Feature[] features) {
		this.features = features;
	}

	public ImageContext getImageContext() {
		return imageContext;
	}

	public void setImageContext(ImageContext imageContext) {
		this.imageContext = imageContext;
	}
}
