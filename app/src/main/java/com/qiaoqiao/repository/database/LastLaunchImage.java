package com.qiaoqiao.repository.database;


import io.realm.RealmObject;

public class LastLaunchImage extends RealmObject {
	private byte[] byteArray;

	public byte[] getByteArray() {
		return byteArray;
	}

	public void setByteArray(byte[] byteArray) {
		this.byteArray = byteArray;
	}
}
