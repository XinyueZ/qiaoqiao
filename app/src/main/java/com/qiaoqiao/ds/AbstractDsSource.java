package com.qiaoqiao.ds;


import android.net.Uri;
import android.support.annotation.NonNull;

import com.qiaoqiao.backend.Service;
import com.qiaoqiao.backend.model.response.AnnotateImageResponseCollection;
import com.qiaoqiao.keymanager.Key;

import java.io.File;

public abstract class AbstractDsSource {
	private @NonNull Service mService;
	private @NonNull Key mKey;

	public AbstractDsSource(@NonNull Service service, @NonNull Key key) {
		mService = service;
		mKey = key;
	}

	@NonNull
	protected Service getService() {
		return mService;
	}

	@NonNull
	protected Key getKey() {
		return mKey;
	}

	public void captureCamera(@NonNull byte[] bytes, @NonNull BytesLoadedCallback callback) {
	}

	public void readLocal(@NonNull File file, @NonNull BytesLoadedCallback callback) {
	}

	public void openWebLink(@NonNull Uri uri, @NonNull OpenWebLinkCallback callback) {
	}


	public static abstract class BytesLoadedCallback {
		public void onVisionResponse(@NonNull AnnotateImageResponseCollection response) {

		}

		public void onError(@NonNull Exception e) {
		}

	}

	public static abstract class OpenWebLinkCallback {
		public void onVisionResponse(@NonNull AnnotateImageResponseCollection response) {

		}
	}

}
