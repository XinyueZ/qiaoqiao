package com.qiaoqiao.ds.local;


import android.support.annotation.NonNull;

import com.google.api.services.vision.v1.model.Status;
import com.qiaoqiao.backend.Google;
import com.qiaoqiao.ds.AbstractDsSource;
import com.qiaoqiao.ds.DsLoadedCallback;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

import javax.inject.Singleton;

@Singleton
public final class DsLocalSource extends AbstractDsSource {

	public DsLocalSource(@NonNull Google google) {
		super(google);
	}

	@Override
	public void onFile(@NonNull File file, @NonNull final DsLoadedCallback callback) {
		try {
			final byte[] bytes = FileUtils.readFileToByteArray(file);
			if (bytes == null) {
				throw new IOException("The bytes of file is NULL:" + file.getAbsolutePath());
			}
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
		} catch (IOException e) {
			callback.onException(e);
		}
	}
}
