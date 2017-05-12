package com.qiaoqiao.backend.model.wikipedia;


import com.google.gson.annotations.SerializedName;

public final class LangLink {
	@SerializedName("lang") private String mLanguage;
	@SerializedName("autonym") private String mAutonym;
	@SerializedName("*") private String mQuery;


	public LangLink(String language, String autonym, String query) {
		mLanguage = language;
		mAutonym = autonym;
		mQuery = query;
	}


	public String getLanguage() {
		return mLanguage;
	}

	public void setLanguage(String language) {
		mLanguage = language;
	}

	public String getAutonym() {
		return mAutonym;
	}

	public void setAutonym(String autonym) {
		mAutonym = autonym;
	}

	public String getQuery() {
		return mQuery;
	}

	public void setQuery(String query) {
		mQuery = query;
	}
}
