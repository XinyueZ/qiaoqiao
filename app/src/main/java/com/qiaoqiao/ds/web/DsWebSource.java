package com.qiaoqiao.ds.web;


import android.net.Uri;
import android.support.annotation.NonNull;

import com.google.api.services.vision.v1.model.Status;
import com.qiaoqiao.backend.Google;
import com.qiaoqiao.ds.AbstractDsSource;

import javax.inject.Singleton;

@Singleton
public final class DsWebSource extends AbstractDsSource {
	public DsWebSource(@NonNull Google google) {
		super(google);
	}

	@Override
	public void onUri(@NonNull final Uri uri, @NonNull final LoadedCallback callback) {
		getGoogle().getAnnotateImageResponse(Google.UriImageBuilder.newBuilder(uri), response -> {
			Status error = response.getResponses()
			                       .get(0)
			                       .getError();
			if (error != null) {
				callback.onError(error);
			} else {
				callback.onVisionResponse(response);
				callback.onSaveHistory(null, uri, response);
			}
		});
	}
}
