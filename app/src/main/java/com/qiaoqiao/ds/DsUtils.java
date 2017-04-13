package com.qiaoqiao.ds;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;


public final class DsUtils {
	private DsUtils() {
	}

	public static byte[] convertBytes(byte[] bytes) throws IOException {
		Bitmap bitmap = scaleBitmapDown(BitmapFactory.decodeByteArray(bytes, 0, bytes.length), 1200);
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.JPEG, 90, byteArrayOutputStream);
		return byteArrayOutputStream.toByteArray();
	}

	public static Bitmap scaleBitmapDown(Bitmap bitmap, int maxDimension) {

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
