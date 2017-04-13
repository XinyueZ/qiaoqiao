package com.qiaoqiao.ds;


import android.net.Uri;
import android.support.annotation.NonNull;

import com.qiaoqiao.backend.Service;
import com.qiaoqiao.keymanager.Key;

import java.io.File;

public abstract class AbstractDsSource {
	private @NonNull Service mService;
	private @NonNull Key mKey;

	public AbstractDsSource(@NonNull Service service,  @NonNull Key key) {
		mService = service;
		mKey = key;
	}

	public void compressData(@NonNull byte[] bytes, @NonNull BytesLoadedCallback callback) {
	}

	public void readLocal(@NonNull File file, BytesLoadedCallback callback) {
	}

	public void openWebLink(@NonNull Uri uri, LoadWebLinkCallback callback) {
	}


	public static abstract class BytesLoadedCallback {
		public void onLoaded(@NonNull final byte[] data) {
		}

		public void onError(@NonNull Exception e) {
		}
	}

	public static abstract class LoadWebLinkCallback {
		public void onLoaded(@NonNull Uri uri) {
		}
	}
}
