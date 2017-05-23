package com.qiaoqiao.utils;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;

import java.io.ByteArrayOutputStream;

public final class ImageUtils {


	private static final int MAX_DIMENSION = 1200;

	private ImageUtils() {
	}

	public static byte[] convertBytes(@NonNull byte[] bytes) {
		Bitmap bitmap = scaleBitmapDown(BitmapFactory.decodeByteArray(bytes, 0, bytes.length), MAX_DIMENSION);
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.JPEG, 90, byteArrayOutputStream);
		return byteArrayOutputStream.toByteArray();
	}

	private static Bitmap scaleBitmapDown(@NonNull Bitmap bitmap, int maxDimension) {

		int originalWidth = bitmap.getWidth();
		int originalHeight = bitmap.getHeight();
		int resizedWidth = maxDimension;
		int resizedHeight = maxDimension;

		if (originalHeight > originalWidth) {
			resizedHeight = maxDimension;
			resizedWidth = (int) (resizedHeight * (float) originalWidth / (float) originalHeight);
		} else if (originalWidth > originalHeight) {
			resizedWidth = maxDimension;
			resizedHeight = (int) (resizedWidth * (float) originalHeight / (float) originalWidth);
		} else if (originalHeight == originalWidth) {
			resizedHeight = maxDimension;
			resizedWidth = maxDimension;
		}
		return Bitmap.createScaledBitmap(bitmap, resizedWidth, resizedHeight, false);
	}
}
