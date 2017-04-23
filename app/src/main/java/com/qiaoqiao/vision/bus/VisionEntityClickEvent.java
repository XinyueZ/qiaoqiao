package com.qiaoqiao.vision.bus;

import android.support.annotation.NonNull;

import com.qiaoqiao.vision.model.VisionEntity;

import javax.annotation.Nonnull;

public final class VisionEntityClickEvent {
	private VisionEntity mEntity;


	public @NonNull
	VisionEntity getEntity() {
		return mEntity;
	}

	public void setEntity(@Nonnull VisionEntity entity) {
		mEntity = entity;
	}
}
