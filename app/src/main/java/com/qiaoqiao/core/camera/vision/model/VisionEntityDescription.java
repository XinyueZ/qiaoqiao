package com.qiaoqiao.core.camera.vision.model;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.google.api.services.vision.v1.model.EntityAnnotation;
import com.google.api.services.vision.v1.model.WebEntity;

public final class VisionEntityDescription {
	private final @NonNull VisionEntity mVisionEntity;

	VisionEntityDescription(@NonNull VisionEntity visionEntity) {
		mVisionEntity = visionEntity;
	}


	public @NonNull
	String getDescriptionText() {
		if (TextUtils.equals(mVisionEntity.getReadableName(), "WEB_DETECTION")) {
			WebEntity webEntity = (WebEntity) mVisionEntity.getVision();
			return webEntity.getDescription();
		}
		if (TextUtils.equals(mVisionEntity.getReadableName(), "LANDMARK_DETECTION")) {
			EntityAnnotation landmarkEntity = (EntityAnnotation) mVisionEntity.getVision();
			return landmarkEntity.getDescription();
		}
		if (TextUtils.equals(mVisionEntity.getReadableName(), "LOGO_DETECTION")) {
			EntityAnnotation logoEntity = (EntityAnnotation) mVisionEntity.getVision();
			return logoEntity.getDescription();
		}
		if (TextUtils.equals(mVisionEntity.getReadableName(), "LABEL_DETECTION")) {
			EntityAnnotation labelEntity = (EntityAnnotation) mVisionEntity.getVision();
			return labelEntity.getDescription();
		}
		return  "N/A" ;
	}

	@Override
	@NonNull
	public String toString() {
		return getDescriptionText();
	}
}
