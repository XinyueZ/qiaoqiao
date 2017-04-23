package com.qiaoqiao.vision.model;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.google.api.services.vision.v1.model.EntityAnnotation;
import com.google.api.services.vision.v1.model.WebEntity;

public final class VisionEntityId {
	private final @NonNull VisionEntity mVisionEntity;

	VisionEntityId(@NonNull VisionEntity visionEntity) {
		mVisionEntity = visionEntity;
	}


	public @Nullable
	String getIdText() {
		if (TextUtils.equals(mVisionEntity.getReadableName(), "WEB_DETECTION")) {
			WebEntity webEntity = (WebEntity) mVisionEntity.getVision();
			return webEntity.getEntityId();
		}
		if (TextUtils.equals(mVisionEntity.getReadableName(), "LANDMARK_DETECTION")) {
			EntityAnnotation landmarkEntity = (EntityAnnotation) mVisionEntity.getVision();
			return landmarkEntity.getMid();
		}
		return null;
	}

	@Override
	@Nullable
	public String toString() {
		return getIdText();
	}
}
