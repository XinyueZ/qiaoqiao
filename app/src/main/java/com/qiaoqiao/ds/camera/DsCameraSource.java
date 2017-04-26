package com.qiaoqiao.ds.camera;


import android.support.annotation.NonNull;

import com.google.api.services.vision.v1.model.Status;
import com.qiaoqiao.backend.Google;
import com.qiaoqiao.ds.AbstractDsSource;

import javax.inject.Singleton;

@Singleton
public final class DsCameraSource extends AbstractDsSource {

	public DsCameraSource(@NonNull Google google) {
		super(google);
	}

	@Override
	public void onBytes(@NonNull final byte[] bytes, @NonNull final LoadedCallback callback) {
		getGoogle().getAnnotateImageResponse(Google.Base64EncodedImageBuilder.newBuilder(bytes), response -> {
			Status error = response.getResponses()
			                       .get(0)
			                       .getError();
			if (error != null) {
				callback.onError(error);
			} else {
				callback.onVisionResponse(response);
				callback.onSaveHistory(bytes, null, response);
			}
		});
	}
}