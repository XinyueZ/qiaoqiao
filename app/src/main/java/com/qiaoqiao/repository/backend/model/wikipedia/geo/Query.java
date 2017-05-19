package com.qiaoqiao.repository.backend.model.wikipedia.geo;


import com.google.gson.annotations.SerializedName;

public final class Query {
	@SerializedName("geosearch")
	private  Geosearch[] mGeosearches;

	public Query(Geosearch[] geosearches) {
		mGeosearches = geosearches;
	}

	public Geosearch[] getGeosearches() {
		return mGeosearches;
	}

	public void setGeosearches(Geosearch[] geosearches) {
		mGeosearches = geosearches;
	}
}
