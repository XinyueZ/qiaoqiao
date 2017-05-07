package com.qiaoqiao.backend.model.wikipedia;


import com.google.gson.annotations.SerializedName;

public final class Query {

	@SerializedName("redirects") private Redirect[] mRedirects;
	@SerializedName("pages") private Pages mPages;

	public Query(Redirect[] redirects, Pages pages) {
		mRedirects = redirects;
		mPages = pages;
	}


	public Redirect[] getRedirects() {
		return mRedirects;
	}

	public void setRedirects(Redirect[] redirects) {
		mRedirects = redirects;
	}

	public Pages getPages() {
		return mPages;
	}

	public void setPages(Pages pages) {
		mPages = pages;
	}
}
