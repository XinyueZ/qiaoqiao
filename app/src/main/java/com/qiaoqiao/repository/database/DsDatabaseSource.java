package com.qiaoqiao.repository.database;

import android.support.annotation.NonNull;

import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.vision.v1.model.BatchAnnotateImagesResponse;
import com.qiaoqiao.repository.AbstractDsSource;
import com.qiaoqiao.repository.DsLoadedCallback;
import com.qiaoqiao.repository.annotation.RepositoryScope;

import java.io.IOException;

import io.realm.Realm;
import io.realm.RealmResults;

@RepositoryScope
public final class DsDatabaseSource extends AbstractDsSource {
	@Override
	public void onRecentRequest(@NonNull DsLoadedCallback callback) {
		final RealmResults<HistoryItem> all = Realm.getDefaultInstance()
		                                           .where(HistoryItem.class)
		                                           .findAll();
		for (HistoryItem item : all) {
			try {
				final BatchAnnotateImagesResponse response = GsonFactory.getDefaultInstance()
				                                                     .createJsonParser(item.getJsonText())
				                                                     .parse(BatchAnnotateImagesResponse.class);
				callback.onVisionResponse(response);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
