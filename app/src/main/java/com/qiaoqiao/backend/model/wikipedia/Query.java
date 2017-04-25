package com.qiaoqiao.backend.model.wikipedia;


import com.google.gson.annotations.SerializedName;

public final class Query {
	@SerializedName("pages")
	private Pages mPages;

	public Query(Pages pages) {
		mPages = pages;
	}


	public Pages getPages() {
		return mPages;
	}

	public void setPages(Pages pages) {
		mPages = pages;
	}
}
