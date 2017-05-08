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
	@SerializedName("thumbnail")
	private Thumbnail mThumbnail;
	@SerializedName("original")
	private Original mOriginal;
	@SerializedName("pageimage")
	private String mPageImage;

	public Page(int pageId, int ns, String title, String extract, Thumbnail thumbnail, Original original, String pageImage) {
		mPageId = pageId;
		mNs = ns;
		mTitle = title;
		mExtract = extract;
		mThumbnail = thumbnail;
		mOriginal = original;
		mPageImage = pageImage;
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

	public Thumbnail getThumbnail() {
		return mThumbnail;
	}

	public void setThumbnail(Thumbnail thumbnail) {
		mThumbnail = thumbnail;
	}

	public Original getOriginal() {
		return mOriginal;
	}

	public void setOriginal(Original original) {
		mOriginal = original;
	}

	public String getPageImage() {
		return mPageImage;
	}

	public void setPageImage(String pageImage) {
		mPageImage = pageImage;
	}
}
