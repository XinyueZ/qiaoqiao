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

	public @NonNull
	VisionEntityId getId() {
		return new VisionEntityId(this);
	}

	@Override
	public String toString() {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("id: ")
		             .append(getId().toString())
		             .append('\n')
		             .append("Description: ")
		             .append(getDescription().toString())
		             .append('\n');
		return stringBuilder.toString();
	}
}
