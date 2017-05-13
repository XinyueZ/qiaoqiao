package com.qiaoqiao.ds.history;


import io.realm.RealmObject;

public   class HistoryItem extends RealmObject{
	private long savedTime;
	private byte[] byteArray;
	private String imageUri;
	private String jsonText;

	public long getSavedTime() {
		return savedTime;
	}

	public void setSavedTime(long savedTime) {
		this.savedTime = savedTime;
	}

	public byte[] getByteArray() {
		return byteArray;
	}

	public void setByteArray(byte[] byteArray) {
		this.byteArray = byteArray;
	}

	public String getImageUri() {
		return imageUri;
	}

	public void setImageUri(String imageUri) {
		this.imageUri = imageUri;
	}

	public String getJsonText() {
		return jsonText;
	}

	public void setJsonText(String jsonText) {
		this.jsonText = jsonText;
	}
}
