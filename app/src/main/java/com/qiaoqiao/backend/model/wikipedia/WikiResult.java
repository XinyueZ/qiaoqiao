package com.qiaoqiao.backend.model.wikipedia;


import com.google.gson.annotations.SerializedName;

public final class WikiResult {
	@SerializedName("query")
	private Query mQuery;


	public WikiResult(Query query) {
		mQuery = query;
	}

	public Query getQuery() {
		return mQuery;
	}

	public void setQuery(Query query) {
		mQuery = query;
	}
}
