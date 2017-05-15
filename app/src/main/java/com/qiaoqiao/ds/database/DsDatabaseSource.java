package com.qiaoqiao.ds.database;

import android.support.annotation.NonNull;

import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.vision.v1.model.BatchAnnotateImagesResponse;
import com.qiaoqiao.ds.AbstractDsSource;
import com.qiaoqiao.ds.DsLoadedCallback;
import com.qiaoqiao.ds.annotation.DsScope;

import java.io.IOException;

import io.realm.Realm;
import io.realm.RealmResults;

@DsScope
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
