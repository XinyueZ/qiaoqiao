package com.qiaoqiao.bus;

import com.google.api.services.vision.v1.model.EntityAnnotation;

public final class EntityClickEvent {
	private EntityAnnotation mEntity;


	public EntityAnnotation getEntity() {
		return mEntity;
	}

	public void setEntity(EntityAnnotation entity) {
		mEntity = entity;
	}
}
