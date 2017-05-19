package com.qiaoqiao.app;


public final class Key {
	private String mApiKey;

	Key(String apiKey) {
		mApiKey = apiKey;
	}

	@Override
	public String toString() {
		return mApiKey;
	}
}
