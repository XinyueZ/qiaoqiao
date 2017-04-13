package com.qiaoqiao.ds.camera;


import android.support.annotation.NonNull;

import com.qiaoqiao.ds.AbstractDsSource;
import com.qiaoqiao.utils.LL;

import java.io.IOException;

import javax.inject.Singleton;

import static com.qiaoqiao.ds.DsUtils.convertBytes;

@Singleton
public final class DsCameraSource extends AbstractDsSource {

	@Override
	public void compressData(@NonNull byte[] bytes, @NonNull BytesLoadedCallback callback) {
		try {
			byte[] imageBytes = convertBytes(bytes);
			callback.onLoaded(imageBytes);
		} catch (IOException e) {
			LL.e(e.toString());
			callback.onError(e);
		}
	}
}
