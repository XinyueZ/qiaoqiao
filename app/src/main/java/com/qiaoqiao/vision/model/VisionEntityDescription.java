package com.qiaoqiao.vision.model;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.google.api.services.vision.v1.model.EntityAnnotation;
import com.google.api.services.vision.v1.model.WebEntity;

public final class VisionEntityDescription {
	private final @NonNull VisionEntity mVisionEntity;

	VisionEntityDescription(@NonNull VisionEntity visionEntity) {
		mVisionEntity = visionEntity;
	}


	public @Nullable
	String getDescriptionText() {
		if (TextUtils.equals(mVisionEntity.getReadableName(), "WEB_DETECTION")) {
			WebEntity webEntity = (WebEntity) mVisionEntity.getVision();
			return webEntity.getDescription();
		}
		if (TextUtils.equals(mVisionEntity.getReadableName(), "LANDMARK_DETECTION")) {
			EntityAnnotation landmarkEntity = (EntityAnnotation) mVisionEntity.getVision();
			return landmarkEntity.getDescription();
		}
		return null;
	}

	@Override
	@Nullable
	public String toString() {
		return getDescriptionText();
	}
}
