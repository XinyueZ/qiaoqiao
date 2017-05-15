package com.qiaoqiao.ds.camera;


import android.support.annotation.NonNull;

import com.google.api.services.vision.v1.model.Status;
import com.qiaoqiao.backend.Google;
import com.qiaoqiao.ds.AbstractDsSource;
import com.qiaoqiao.ds.DsLoadedCallback;
import com.qiaoqiao.ds.annotation.DsScope;

@DsScope
public final class DsCameraSource extends AbstractDsSource {

	public DsCameraSource(@NonNull Google google) {
		super(google);
	}

	@Override
	public void onBytes(@NonNull final byte[] bytes, @NonNull final DsLoadedCallback callback) {
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