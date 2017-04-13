package com.qiaoqiao.ds.web;


import android.net.Uri;
import android.support.annotation.NonNull;

import com.qiaoqiao.ds.AbstractDsSource;

import javax.inject.Singleton;

@Singleton
public final class DsWebSource extends AbstractDsSource {

	@Override
	public void openWebLink(@NonNull Uri uri, LoadWebLinkCallback callback) {
		callback.onLoaded(uri);
	}
}
