package com.qiaoqiao.backend.model.request;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public final class AnnotateImageRequest {
	@SerializedName("image") private Image image;
	@SerializedName("features") private List<Feature> features;
	@SerializedName("imageContext") private ImageContext imageContext;


	public AnnotateImageRequest(Image image, List<Feature> features, ImageContext imageContext) {
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

	public List<Feature> getFeatures() {
		return features;
	}

	public void setFeatures(List<Feature> features) {
		this.features = features;
	}

	public ImageContext getImageContext() {
		return imageContext;
	}

	public void setImageContext(ImageContext imageContext) {
		this.imageContext = imageContext;
	}
}
