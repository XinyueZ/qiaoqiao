package com.qiaoqiao.app;


import java.io.Serializable;

public final class Key implements Serializable{
	private String mApiKey;

	Key(String apiKey) {
		mApiKey = apiKey;
	}

	@Override
	public String toString() {
		return mApiKey;
	}
}
