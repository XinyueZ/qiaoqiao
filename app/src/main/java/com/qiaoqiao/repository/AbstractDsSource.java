package com.qiaoqiao.repository;


import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.google.android.gms.maps.model.LatLng;
import com.qiaoqiao.repository.backend.Google;
import com.qiaoqiao.repository.backend.ImageProvider;
import com.qiaoqiao.repository.backend.Wikipedia;
import com.qiaoqiao.repository.backend.model.wikipedia.LangLink;
import com.qiaoqiao.repository.database.LastLaunchImage;
import com.qiaoqiao.utils.LL;

import java.io.File;

import io.realm.Realm;

public abstract class AbstractDsSource {
	private Google mGoogle;
	private com.qiaoqiao.repository.backend.Wikipedia mWikipedia;
	private ImageProvider mImageProvider;

	protected AbstractDsSource() {

	}

	protected AbstractDsSource(@NonNull ImageProvider imageProvider) {
		mImageProvider = imageProvider;
	}

	protected AbstractDsSource(@NonNull Google google) {
		mGoogle = google;
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


	public ImageProvider getImageProvider() {
		return mImageProvider;
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

	public void onImage(@NonNull Context cxt, @NonNull DsLoadedCallback callback) {

	}

	public void saveLoadedLaunchImage(@NonNull byte[] imageData) {
		final Realm realm = Realm.getDefaultInstance();
		realm.executeTransactionAsync(bgRealm -> {
			LastLaunchImage lastLaunchImage = bgRealm.createObject(LastLaunchImage.class);
			lastLaunchImage.setByteArray(imageData);
		}, () -> LL.d("Saved last launch-image successfully."), error -> LL.d("Saved launch-image fail."));
	}
}
