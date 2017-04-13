package com.qiaoqiao.backend.model.request;

import com.google.gson.annotations.SerializedName;


public final class Feature {
	@SerializedName("type") private String type;
	@SerializedName("maxResults") private int maxResults;

	public Feature(String type, int maxResults) {
		this.type = type;
		this.maxResults = maxResults;
	}


	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getMaxResults() {
		return maxResults;
	}

	public void setMaxResults(int maxResults) {
		this.maxResults = maxResults;
	}
}
