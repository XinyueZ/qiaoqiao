package com.qiaoqiao.vision;


public final class VisionEntity<T> {
	private T mVision;
	private String mReadableName;

	public VisionEntity(T vision, String readableName) {
		mVision = vision;
		mReadableName = readableName;
	}


	public T getVision() {
		return mVision;
	}

	public String getReadableName() {
		return mReadableName;
	}
}
