package com.qiaoqiao.core.camera.vision.model;


import android.support.annotation.NonNull;

import com.google.api.client.json.GenericJson;

public final class VisionEntity {
	private final @NonNull GenericJson mVision;
	private final @NonNull String mReadableName;
	private boolean mActivated;

	private final boolean mInCell;

	public VisionEntity(@NonNull GenericJson vision, @NonNull String readableName) {
		mVision = vision;
		mReadableName = readableName;
		mInCell = false;
	}

	public VisionEntity(@NonNull GenericJson vision, @NonNull String readableName, boolean inCell) {
		mVision = vision;
		mReadableName = readableName;
		mInCell = inCell;
	}

	public boolean isInCell() {
		return mInCell;
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

	public @NonNull
	VisionEntityLocation getLocation() {
		return new VisionEntityLocation(this);
	}



	public boolean isActivated() {
		return mActivated;
	}

	public VisionEntity setActivated(boolean activated) {
		mActivated = activated;
		return this;
	}

	@Override
	public String toString() {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("id: ")
		             .append(getId().toString())
		             .append('\n')
		             .append("location:")
		             .append(getLocation().toString())
		             .append("Description: ")
		             .append(getDescription().toString())
		             .append('\n');
		return stringBuilder.toString();
	}
}
