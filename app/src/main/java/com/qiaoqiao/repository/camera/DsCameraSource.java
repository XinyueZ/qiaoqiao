package com.qiaoqiao.repository.camera;


import android.support.annotation.NonNull;

import com.google.api.services.vision.v1.model.Status;
import com.qiaoqiao.repository.backend.Google;
import com.qiaoqiao.repository.AbstractDsSource;
import com.qiaoqiao.repository.DsLoadedCallback;
import com.qiaoqiao.repository.annotation.RepositoryScope;

@RepositoryScope
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