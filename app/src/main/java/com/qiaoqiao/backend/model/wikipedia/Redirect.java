package com.qiaoqiao.backend.model.wikipedia;


import com.google.gson.annotations.SerializedName;

public final class Redirect {
	@SerializedName("from")
	private String mFrom;
	@SerializedName("to")
	private String mTo;


	public Redirect(String from, String to) {
		mFrom = from;
		mTo = to;
	}


	public String getFrom() {
		return mFrom;
	}

	public void setFrom(String from) {
		mFrom = from;
	}

	public String getTo() {
		return mTo;
	}

	public void setTo(String to) {
		mTo = to;
	}
}
