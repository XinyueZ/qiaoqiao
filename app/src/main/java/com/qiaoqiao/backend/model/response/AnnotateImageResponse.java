package com.qiaoqiao.backend.model.response;


import com.google.gson.annotations.SerializedName;
import com.qiaoqiao.backend.model.response.landmark.EntityAnnotation;
import com.qiaoqiao.backend.model.response.web.WebDetection;

public final class AnnotateImageResponse {
	@SerializedName("landmarkAnnotations") private EntityAnnotation[] landmarkAnnotations;
	@SerializedName("webDetection") private WebDetection webDetection;


	public AnnotateImageResponse(EntityAnnotation[] landmarkAnnotations, WebDetection webDetection) {
		this.landmarkAnnotations = landmarkAnnotations;
		this.webDetection = webDetection;
	}

	public EntityAnnotation[] getLandmarkAnnotations() {
		return landmarkAnnotations;
	}

	public void setLandmarkAnnotations(EntityAnnotation[] landmarkAnnotations) {
		this.landmarkAnnotations = landmarkAnnotations;
	}

	public WebDetection getWebDetection() {
		return webDetection;
	}

	public void setWebDetection(WebDetection webDetection) {
		this.webDetection = webDetection;
	}
}
