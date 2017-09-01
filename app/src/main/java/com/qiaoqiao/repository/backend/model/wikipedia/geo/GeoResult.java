package com.qiaoqiao.repository.backend.model.wikipedia.geo;


import com.google.gson.annotations.SerializedName;

public class GeoResult {
	@SerializedName("query")
	private Query mQuery;

	public GeoResult(Query query) {
		mQuery = query;
	}


	public Query getQuery() {
		return mQuery;
	}

	public void setQuery(Query query) {
		mQuery = query;
	}
}
