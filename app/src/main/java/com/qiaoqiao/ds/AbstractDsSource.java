package com.qiaoqiao.ds;


import android.net.Uri;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.google.api.services.vision.v1.model.BatchAnnotateImagesResponse;
import com.google.api.services.vision.v1.model.Status;
import com.qiaoqiao.backend.Service;
import com.qiaoqiao.utils.LL;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

public abstract class AbstractDsSource {
	private @NonNull final Service mService;

	public AbstractDsSource(@NonNull Service service) {
		mService = service;
	}

	@NonNull
	protected Service getService() {
		return mService;
	}


	public void onBytes(@NonNull byte[] bytes, @NonNull LoadedCallback callback) {
	}

	public void onFile(@NonNull File file, @NonNull LoadedCallback callback) {
	}

	public void onUri(@NonNull Uri uri, @NonNull LoadedCallback callback) {
	}


	public static abstract class LoadedCallback {
		public void onVisionResponse(BatchAnnotateImagesResponse response) {
			if (response == null) {
				LL.d("response is NULL.");
				return;
			}
			try {
				LL.d("response is OK.");
				LL.d(response.toPrettyString());
			} catch (IOException e) {
				LL.d("response is available but IO problem: " + e.toString());
			}
		}

		public void onError(@NonNull Status status) {
			if (!TextUtils.isEmpty(status.getMessage())) {
				LL.e(status.getMessage());
			} else {
				LL.e("Vision API on some error:");
				final List<Map<String, Object>> details = status.getDetails();
				for (Map<String, Object> mapDetail : details) {
					Set<String> mapDetailKeys = mapDetail.keySet();
					for (String key : mapDetailKeys) {
						LL.e(mapDetail.get(key)
						              .toString());
					}
				}
			}
		}

		public void onException(@NonNull Exception e) {
			LL.e(e.toString());
		}

	}
}
