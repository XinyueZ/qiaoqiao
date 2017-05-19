package com.qiaoqiao.core.camera.vision.bus;

import android.support.annotation.NonNull;
import android.view.View;

import com.qiaoqiao.core.camera.vision.model.VisionEntity;

import javax.annotation.Nonnull;

public final class VisionEntityClickEvent {
	private VisionEntity mEntity;
	private View  mTransitionView;


	public @NonNull
	VisionEntity getEntity() {
		return mEntity;
	}

	public void setEntity(@Nonnull VisionEntity entity) {
		mEntity = entity;
	}

	public View getTransitionView() {
		return mTransitionView;
	}

	public void setTransitionView(View transitionView) {
		mTransitionView = transitionView;
	}
}
