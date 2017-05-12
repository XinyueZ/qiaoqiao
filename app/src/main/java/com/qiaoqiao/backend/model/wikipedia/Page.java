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
	private Image mThumbnail;
	@SerializedName("original")
	private Image mOriginal;
	@SerializedName("pageimage")
	private String mPageImage;
	@SerializedName("langlinks")
	private LangLink[] mLangLinks;

	public Page(int pageId, int ns, String title, String extract, Image thumbnail, Image original, String pageImage, LangLink[] langLinks) {
		mPageId = pageId;
		mNs = ns;
		mTitle = title;
		mExtract = extract;
		mThumbnail = thumbnail;
		mOriginal = original;
		mPageImage = pageImage;
		mLangLinks = langLinks;
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

	public Image getThumbnail() {
		return mThumbnail;
	}

	public void setThumbnail(Image thumbnail) {
		mThumbnail = thumbnail;
	}

	public Image getOriginal() {
		return mOriginal;
	}

	public void setOriginal(Image original) {
		mOriginal = original;
	}

	public String getPageImage() {
		return mPageImage;
	}

	public void setPageImage(String pageImage) {
		mPageImage = pageImage;
	}

	public LangLink[] getLangLinks() {
		return mLangLinks;
	}

	public void setLangLinks(LangLink[] langLinks) {
		mLangLinks = langLinks;
	}
}
