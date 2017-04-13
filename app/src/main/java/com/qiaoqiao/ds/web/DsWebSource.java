package com.qiaoqiao.ds.web;


import android.net.Uri;
import android.support.annotation.NonNull;

import com.qiaoqiao.backend.Service;
import com.qiaoqiao.ds.AbstractDsSource;
import com.qiaoqiao.keymanager.Key;

import javax.inject.Singleton;

@Singleton
public final class DsWebSource extends AbstractDsSource {
	public DsWebSource(@NonNull Service service,  @NonNull Key key) {
		super(service, key);
	}

	@Override
	public void openWebLink(@NonNull Uri uri, OpenWebLinkCallback callback) {
		callback.onOpened(uri);
		callback.onVisionResponse(null);
	}
}
