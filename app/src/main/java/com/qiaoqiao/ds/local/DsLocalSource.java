package com.qiaoqiao.ds.local;


import android.support.annotation.NonNull;

import com.qiaoqiao.backend.Service;
import com.qiaoqiao.backend.model.request.AnnotateImageRequest;
import com.qiaoqiao.backend.model.request.AnnotateImageRequestCollection;
import com.qiaoqiao.backend.model.request.Feature;
import com.qiaoqiao.backend.model.request.Image;
import com.qiaoqiao.backend.model.response.AnnotateImageResponseCollection;
import com.qiaoqiao.ds.AbstractDsSource;
import com.qiaoqiao.keymanager.Key;
import com.qiaoqiao.utils.LL;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

import javax.inject.Singleton;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import static com.qiaoqiao.ds.DsUtils.convertBytes;

@Singleton
public final class DsLocalSource extends AbstractDsSource {

	public DsLocalSource(@NonNull Service service, @NonNull Key key) {
		super(service, key);
	}

	@Override
	public void readLocal(@NonNull File file, @NonNull final BytesLoadedCallback callback) {
		try {
			AnnotateImageRequest request = new AnnotateImageRequest(new Image(convertBytes(FileUtils.readFileToByteArray(file)), null),
			                                                        null,
			                                                        new Feature("WEB_DETECTION", 5),
			                                                        new Feature("LANDMARK_DETECTION", 5));
			AnnotateImageRequestCollection visionRequest = new AnnotateImageRequestCollection(request);
			getService().getAnnotateImageResponse(getKey().toString(), visionRequest)
			            .subscribeOn(Schedulers.io())
			            .observeOn(AndroidSchedulers.mainThread())
			            .subscribe(new Consumer<AnnotateImageResponseCollection>() {
				            @Override
				            public void accept(@NonNull AnnotateImageResponseCollection response) throws Exception {
					            callback.onVisionResponse(response);
				            }
			            });
		} catch (IOException e) {
			LL.e(e.toString());
			callback.onError(e);
		}
	}
}
