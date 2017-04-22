package com.qiaoqiao.vision.model;


import android.support.annotation.NonNull;

import com.google.api.client.json.GenericJson;

public final class VisionEntity {
	private final @NonNull GenericJson mVision;
	private final @NonNull String mReadableName;

	public VisionEntity(@NonNull GenericJson vision, @NonNull String readableName) {
		mVision = vision;
		mReadableName = readableName;
	}


	@NonNull
	GenericJson getVision() {
		return mVision;
	}

	public @NonNull
	String getReadableName() {
		return mReadableName;
	}

	public @NonNull
	VisionEntityDescription getDescription() {
		return new VisionEntityDescription(this);
	}

	@Override
	public String toString() {
		return getDescription().toString();
	}
}
