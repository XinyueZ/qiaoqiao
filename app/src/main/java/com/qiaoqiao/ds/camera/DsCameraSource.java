package com.qiaoqiao.ds.camera;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.annotation.NonNull;

import com.qiaoqiao.ds.DsSource;
import com.qiaoqiao.ds.DsType;

import java.io.ByteArrayOutputStream;
import java.io.File;

import javax.inject.Singleton;

import static com.qiaoqiao.app.Values.TEMP_PHOTO_NAME;
import static com.qiaoqiao.ds.DsUtils.scaleBitmapDown;

@Singleton
public final class DsCameraSource implements DsSource {
	private final static @DsType int sDsType = DsType.CAMERA;

	@Override
	public void loadData(@NonNull Context cxt, @DsType int dsType, @NonNull    final DataLoadedCallback callback) {
		if (sDsType == dsType) {
			File file = new File(cxt.getExternalFilesDir(Environment.DIRECTORY_PICTURES), TEMP_PHOTO_NAME);
			Bitmap bitmap = scaleBitmapDown(BitmapFactory.decodeFile(file.getAbsolutePath()), 1200);
			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			bitmap.compress(Bitmap.CompressFormat.JPEG, 90, byteArrayOutputStream);
			byte[] imageBytes = byteArrayOutputStream.toByteArray();
			callback.onLoaded(imageBytes);
		}
	}


}
