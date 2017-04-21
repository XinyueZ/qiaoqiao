package com.qiaoqiao.ds.local;


import android.support.annotation.NonNull;

import com.google.api.services.vision.v1.model.BatchAnnotateImagesResponse;
import com.google.api.services.vision.v1.model.Status;
import com.qiaoqiao.backend.Service;
import com.qiaoqiao.ds.AbstractDsSource;
import com.qiaoqiao.utils.LL;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

import javax.inject.Singleton;

import io.reactivex.functions.Consumer;

@Singleton
public final class DsLocalSource extends AbstractDsSource {

	public DsLocalSource(@NonNull Service service) {
		super(service);
	}

	@Override
	public void onFile(@NonNull File file, @NonNull final LoadedCallback callback) {
		try {
			byte[] bytes = FileUtils.readFileToByteArray(file);
			if (bytes == null) {
				throw new IOException("The bytes of file is NULL:" + file.getAbsolutePath());
			}
			getService().getAnnotateImageResponse(Service.Base64EncodedImageBuilder.newBuilder(bytes), new Consumer<BatchAnnotateImagesResponse>() {
				@Override
				public void accept(BatchAnnotateImagesResponse response) throws Exception {
					Status error = response.getResponses()
					                       .get(0)
					                       .getError();
					if (error != null) {
						callback.onError(error);
					} else {
						callback.onVisionResponse(response);
					}
				}
			});
		} catch (IOException e) {
			callback.onException(e);
		}
	}
}
