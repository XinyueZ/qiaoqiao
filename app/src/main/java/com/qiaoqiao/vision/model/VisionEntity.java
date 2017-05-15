package com.qiaoqiao.vision.model;


import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.api.client.json.GenericJson;

public final class VisionEntity {
	private final @NonNull GenericJson mVision;
	private final @NonNull String mReadableName;
	private @Nullable Uri mImageUri = null;
	private boolean mInCell;

	public VisionEntity(@NonNull GenericJson vision, @NonNull String readableName) {
		mVision = vision;
		mReadableName = readableName;
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

	public @Nullable
	Uri getImageUri() {
		return mImageUri;
	}

	public void setImageUri(@NonNull  Uri imageUri) {
		mImageUri = imageUri;
	}

	public void setInCell(boolean inCell) {
		mInCell = inCell;
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
