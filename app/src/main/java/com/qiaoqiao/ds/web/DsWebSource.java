package com.qiaoqiao.ds.web;


import android.net.Uri;
import android.support.annotation.NonNull;

import com.google.api.services.vision.v1.model.BatchAnnotateImagesResponse;
import com.google.api.services.vision.v1.model.Status;
import com.qiaoqiao.backend.Service;
import com.qiaoqiao.ds.AbstractDsSource;

import javax.inject.Singleton;

import io.reactivex.functions.Consumer;

@Singleton
public final class DsWebSource extends AbstractDsSource {
	public DsWebSource(@NonNull Service service) {
		super(service);
	}

	@Override
	public void onUri(@NonNull Uri uri, @NonNull final LoadedCallback callback) {
		getService().getAnnotateImageResponse(Service.UriImageBuilder.newBuilder(uri), new Consumer<BatchAnnotateImagesResponse>() {
			@Override
			public void accept(BatchAnnotateImagesResponse response) throws Exception {
				Status error = response.getResponses()
				                       .get(0)
				                       .getError();
				if (error != null) {
					callback.onError(error);
				} else {
					callback.onVisionResponse(response);
					callback.onSaveHistory(null, null, response);
				}
			}
		});
	}
}
