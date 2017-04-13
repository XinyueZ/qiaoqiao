package com.qiaoqiao.ds.camera;


import android.net.Uri;
import android.support.annotation.NonNull;

import com.qiaoqiao.ds.DsSource;

import java.io.File;
import java.io.IOException;

import javax.inject.Singleton;

import static com.qiaoqiao.ds.DsUtils.convertBytes;

@Singleton
public final class DsCameraSource implements DsSource {

	@Override
	public void compressData(@NonNull byte[] bytes, @NonNull BytesLoadedCallback callback) {
		try {
			byte[] imageBytes = convertBytes(bytes);
			callback.onLoaded(imageBytes);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void readLocal(@NonNull File file, BytesLoadedCallback callback) {

	}

	@Override
	public void readWeb(@NonNull Uri uri, LocalLoadedCallback callback) {

	}
}
