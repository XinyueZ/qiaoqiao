package com.qiaoqiao.ds;


import android.net.Uri;
import android.support.annotation.NonNull;

import java.io.File;

public interface DsSource {
	void compressData(@NonNull byte[] bytes, @NonNull BytesLoadedCallback callback);

	void readLocal(@NonNull File file, BytesLoadedCallback callback);

	void readWeb(@NonNull Uri uri, LocalLoadedCallback callback);


	interface BytesLoadedCallback {
		void onLoaded(@NonNull final byte[] data);
	}

	interface LocalLoadedCallback {
		void onLoaded(@NonNull Uri uri);
	}
}
