package com.qiaoqiao.ds.web;


import android.net.Uri;
import android.support.annotation.NonNull;

import com.qiaoqiao.ds.DsSource;

import java.io.File;

import javax.inject.Singleton;

@Singleton
public final class DsWebSource implements DsSource {

	@Override
	public void readWeb(@NonNull Uri uri, LocalLoadedCallback callback) {
		callback.onLoaded(uri);
	}

	@Override
	public void compressData(@NonNull byte[] bytes, @NonNull BytesLoadedCallback callback) {

	}

	@Override
	public void readLocal(@NonNull File file, BytesLoadedCallback callback) {

	}

}
