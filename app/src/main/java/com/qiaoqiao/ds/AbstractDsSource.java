package com.qiaoqiao.ds;


import android.net.Uri;
import android.support.annotation.NonNull;

import com.qiaoqiao.backend.Google;
import com.qiaoqiao.backend.Wikipedia;
import com.qiaoqiao.backend.model.wikipedia.LangLink;
import com.qiaoqiao.vision.model.VisionEntity;

import java.io.File;
import java.io.IOException;
import java.util.List;

public abstract class AbstractDsSource {
	private Google mGoogle;
	private com.qiaoqiao.backend.Wikipedia mWikipedia;

	protected AbstractDsSource() {

	}


	public AbstractDsSource(@NonNull Google google) {
		mGoogle = google;
	}

	public AbstractDsSource(@NonNull Wikipedia wikipedia) {
		mWikipedia = wikipedia;
	}


	public AbstractDsSource(@NonNull Google google, @NonNull Wikipedia wikipedia) {
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

	public void onKnowledgeQuery(@NonNull String keyword, @NonNull DsLoadedCallback callback) {

	}

	public void onKnowledgeQuery(@NonNull LangLink langLink, @NonNull DsLoadedCallback callback) {

	}

	public void onKnowledgeQuery(@NonNull List<VisionEntity> list) throws IOException {

	}

	public void onTranslate(@NonNull String q, @NonNull DsLoadedCallback callback) {

	}

	public void onRecentRequest(@NonNull DsLoadedCallback callback) {

	}
}
