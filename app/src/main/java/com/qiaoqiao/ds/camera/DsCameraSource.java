package com.qiaoqiao.ds.camera;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.qiaoqiao.ds.DsSource;

import java.io.ByteArrayOutputStream;
import java.io.File;

import javax.inject.Singleton;

import static com.qiaoqiao.ds.DsUtils.scaleBitmapDown;

@Singleton
public final class DsCameraSource implements DsSource {

	@Override
	public void compressData(@NonNull byte[] bytes, @NonNull BytesLoadedCallback callback) {
		Bitmap bitmap = scaleBitmapDown(BitmapFactory.decodeByteArray(bytes, 0, bytes.length), 1200);
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.JPEG, 90, byteArrayOutputStream);
		byte[] imageBytes = byteArrayOutputStream.toByteArray();
		callback.onLoaded(imageBytes);
	}

	@Override
	public void readLocal(@NonNull File file, BytesLoadedCallback callback) {

	}

	@Override
	public void readWeb(@NonNull Uri uri, LocalLoadedCallback callback) {

	}
}
