package com.qiaoqiao.ds.camera;


import android.support.annotation.NonNull;

import com.qiaoqiao.backend.Service;
import com.qiaoqiao.ds.AbstractDsSource;
import com.qiaoqiao.keymanager.Key;
import com.qiaoqiao.utils.LL;

import java.io.IOException;

import javax.inject.Singleton;

import static com.qiaoqiao.ds.DsUtils.convertBytes;

@Singleton
public final class DsCameraSource extends AbstractDsSource {

	public DsCameraSource(@NonNull Service service, @NonNull Key key) {
		super(service, key);
	}

	@Override
	public void captureCamera(@NonNull byte[] bytes, @NonNull BytesLoadedCallback callback) {
		try {
			byte[] imageBytes = convertBytes(bytes);
			callback.onVisionResponse(null);
		} catch (IOException e) {
			LL.e(e.toString());
			callback.onError(e);
		}
	}
}
