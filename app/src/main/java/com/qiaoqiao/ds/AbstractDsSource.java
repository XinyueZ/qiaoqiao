package com.qiaoqiao.ds;


import android.net.Uri;
import android.support.annotation.NonNull;

import java.io.File;

public abstract class AbstractDsSource {
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
