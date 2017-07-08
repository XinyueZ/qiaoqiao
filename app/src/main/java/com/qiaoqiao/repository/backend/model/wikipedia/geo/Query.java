package com.qiaoqiao.repository.backend.model.wikipedia.geo;


import com.google.gson.annotations.SerializedName;

import java.util.List;

public final class Query {
	@SerializedName("geosearch")
	private List<Geosearch> mGeosearches;

	public Query(List<Geosearch> geosearches) {
		mGeosearches = geosearches;
	}

	public List<Geosearch> getGeosearches() {
		return mGeosearches;
	}

	public void setGeosearches(List<Geosearch> geosearches) {
		mGeosearches = geosearches;
	}
}
