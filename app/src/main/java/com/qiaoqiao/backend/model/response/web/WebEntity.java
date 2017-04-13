package com.qiaoqiao.backend.model.response.web;


import com.google.gson.annotations.SerializedName;

public final class WebEntity {
	@SerializedName("entityId") private String entityId;
	@SerializedName("score") private double score;
	@SerializedName("description") private String description;


	public WebEntity(String entityId, double score, String description) {
		this.entityId = entityId;
		this.score = score;
		this.description = description;
	}


	public String getEntityId() {
		return entityId;
	}

	public void setEntityId(String entityId) {
		this.entityId = entityId;
	}

	public double getScore() {
		return score;
	}

	public void setScore(double score) {
		this.score = score;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
