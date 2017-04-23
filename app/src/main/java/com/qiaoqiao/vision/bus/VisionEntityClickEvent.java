package com.qiaoqiao.vision.bus;

import com.qiaoqiao.vision.model.VisionEntity;

public final class VisionEntityClickEvent {
	private VisionEntity mEntity;


	public VisionEntity getEntity() {
		return mEntity;
	}

	public void setEntity(VisionEntity entity) {
		mEntity = entity;
	}
}
