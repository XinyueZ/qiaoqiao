package com.qiaoqiao.repository;


import android.net.Uri;
import android.support.annotation.NonNull;

import com.google.android.gms.maps.model.LatLng;
import com.qiaoqiao.repository.backend.Google;
import com.qiaoqiao.repository.backend.Wikipedia;
import com.qiaoqiao.repository.backend.model.wikipedia.LangLink;

import java.io.File;

public abstract class AbstractDsSource {
	private Google mGoogle;
	private com.qiaoqiao.repository.backend.Wikipedia mWikipedia;

	protected AbstractDsSource() {

	}


	protected AbstractDsSource(@NonNull Google google) {
		mGoogle = google;
	}

	protected AbstractDsSource(@NonNull Wikipedia wikipedia) {
		mWikipedia = wikipedia;
	}


	protected AbstractDsSource(@NonNull Google google, @NonNull Wikipedia wikipedia) {
		mGoogle = google;
		mWikipedia = wikipedia;
	}

	protected Google getGoogle() {
		return mGoogle;
	}

	protected Wikipedia getWikipedia() {
		return mWikipedia;
	}

	public void onBytes(@NonNull byte[] bytes, @NonNull DsLoadedCallback callback) {
	}

	public void onFile(@NonNull File file, @NonNull DsLoadedCallback callback) {
	}

	public void onUri(@NonNull Uri uri, @NonNull DsLoadedCallback callback) {
	}

	public void onKnowledgeQuery(int pageId, @NonNull DsLoadedCallback callback) {

	}

	public void onKnowledgeQuery(@NonNull String keyword, @NonNull DsLoadedCallback callback) {

	}

	public void onKnowledgeQuery(@NonNull LangLink langLink, @NonNull DsLoadedCallback callback) {

	}

	public void onGeosearchQuery(@NonNull LatLng latLng, long radius, @NonNull DsLoadedCallback callback) {

	}


	public void onTranslate(@NonNull String q, @NonNull DsLoadedCallback callback) {

	}

	public void onRecentRequest(@NonNull DsLoadedCallback callback) {

	}
}
