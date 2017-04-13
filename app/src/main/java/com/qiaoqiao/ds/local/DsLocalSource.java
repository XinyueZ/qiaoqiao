package com.qiaoqiao.ds.local;


import android.net.Uri;
import android.support.annotation.NonNull;

import com.qiaoqiao.ds.DsSource;
import com.qiaoqiao.utils.LL;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

import javax.inject.Singleton;

import static com.qiaoqiao.ds.DsUtils.convertBytes;

@Singleton
public final class DsLocalSource implements DsSource {


	@Override
	public void readLocal(@NonNull File file, BytesLoadedCallback callback) {
		try {
			byte bytes[] = FileUtils.readFileToByteArray(file);
			byte[] imageBytes = convertBytes(bytes);
			callback.onLoaded(imageBytes);
		} catch (IOException e) {
			LL.e(e.toString());
		}
	}



	@Override
	public void compressData(@NonNull byte[] bytes, @NonNull BytesLoadedCallback callback) {

	}

	@Override
	public void readWeb(@NonNull Uri uri, LocalLoadedCallback callback) {

	}
}
