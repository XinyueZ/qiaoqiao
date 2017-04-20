package com.qiaoqiao.ds.web;


import android.net.Uri;
import android.support.annotation.NonNull;

import com.qiaoqiao.backend.Service;
import com.qiaoqiao.backend.model.request.AnnotateImageRequest;
import com.qiaoqiao.backend.model.request.AnnotateImageRequestCollection;
import com.qiaoqiao.backend.model.request.Feature;
import com.qiaoqiao.backend.model.request.Image;
import com.qiaoqiao.backend.model.request.ImageSource;
import com.qiaoqiao.backend.model.response.AnnotateImageResponseCollection;
import com.qiaoqiao.ds.AbstractDsSource;
import com.qiaoqiao.keymanager.Key;

import javax.inject.Singleton;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

@Singleton
public final class DsWebSource extends AbstractDsSource {
	public DsWebSource(@NonNull Service service, @NonNull Key key) {
		super(service, key);
	}

	@Override
	public void openWebLink(@NonNull Uri uri, @NonNull final OpenWebLinkCallback callback) {
		AnnotateImageRequest request = new AnnotateImageRequest(new Image(null, new ImageSource(null, uri.toString())), null, new Feature("WEB_DETECTION", 5), new Feature("LANDMARK_DETECTION", 5));
		AnnotateImageRequestCollection visionRequest = new AnnotateImageRequestCollection(request);
		getService().getAnnotateImageResponse(getKey().toString(), visionRequest)
		            .subscribeOn(Schedulers.io())
		            .observeOn(AndroidSchedulers.mainThread())
		            .subscribe(new Consumer<AnnotateImageResponseCollection>() {
			            @Override
			            public void accept(@NonNull AnnotateImageResponseCollection response) throws Exception {
				            callback.onVisionResponse(null);
			            }
		            });
	}
}
