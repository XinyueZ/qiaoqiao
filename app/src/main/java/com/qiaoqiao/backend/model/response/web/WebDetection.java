package com.qiaoqiao.backend.model.response.web;


import com.google.gson.annotations.SerializedName;

public final class WebDetection {
	@SerializedName("webEntities") private WebEntity[] webEntities;

	public WebDetection(WebEntity[] webEntities) {
		this.webEntities = webEntities;
	}

	public WebEntity[] getWebEntities() {
		return webEntities;
	}

	public void setWebEntities(WebEntity[] webEntities) {
		this.webEntities = webEntities;
	}
}
