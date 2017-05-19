package com.qiaoqiao.repository.web;


import android.net.Uri;
import android.support.annotation.NonNull;

import com.google.api.services.vision.v1.model.Status;
import com.qiaoqiao.repository.backend.Google;
import com.qiaoqiao.repository.AbstractDsSource;
import com.qiaoqiao.repository.DsLoadedCallback;
import com.qiaoqiao.repository.annotation.RepositoryScope;

@RepositoryScope
public final class DsWebSource extends AbstractDsSource {
	public DsWebSource(@NonNull Google google) {
		super(google);
	}

	@Override
	public void onUri(@NonNull final Uri uri, @NonNull final DsLoadedCallback callback) {
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
