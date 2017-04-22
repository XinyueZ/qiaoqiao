package com.qiaoqiao.vision.model;


public final class VisionEntity {
	private final Object mVision;
	private final String mReadableName;

	public VisionEntity(Object vision, String readableName) {
		mVision = vision;
		mReadableName = readableName;
	}


	Object getVision() {
		return mVision;
	}

	public String getReadableName() {
		return mReadableName;
	}

	public VisionEntityDescription getDescription() {
		return new VisionEntityDescription(this);
	}
}
