package com.qiaoqiao.backend.model.wikipedia;


import com.google.gson.annotations.SerializedName;

public final class Page {
	@SerializedName("pageid")
	private int mPageId;
	@SerializedName("ns")
	private int mNs;
	@SerializedName("title")
	private String mTitle;
	@SerializedName("extract")
	private String mExtract;


	public Page(int pageId, int ns, String title, String extract) {
		mPageId = pageId;
		mNs = ns;
		mTitle = title;
		mExtract = extract;
	}

	public int getPageId() {
		return mPageId;
	}

	public void setPageId(int pageId) {
		mPageId = pageId;
	}

	public int getNs() {
		return mNs;
	}

	public void setNs(int ns) {
		mNs = ns;
	}

	public String getTitle() {
		return mTitle;
	}

	public void setTitle(String title) {
		mTitle = title;
	}

	public String getExtract() {
		return mExtract;
	}

	public void setExtract(String extract) {
		mExtract = extract;
	}
}
