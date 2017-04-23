package com.qiaoqiao.ds;


import android.net.Uri;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.google.api.services.vision.v1.model.BatchAnnotateImagesResponse;
import com.google.api.services.vision.v1.model.Status;
import com.qiaoqiao.backend.Service;
import com.qiaoqiao.ds.database.HistoryItem;
import com.qiaoqiao.utils.LL;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import io.realm.Realm;

public abstract class AbstractDsSource {
	private @NonNull final Service mService;

	public AbstractDsSource(@NonNull Service service) {
		mService = service;
	}

	@NonNull
	protected Service getService() {
		return mService;
	}


	public void onBytes(@NonNull byte[] bytes, @NonNull LoadedCallback callback) {
	}

	public void onFile(@NonNull File file, @NonNull LoadedCallback callback) {
	}

	public void onUri(@NonNull Uri uri, @NonNull LoadedCallback callback) {
	}


	public static abstract class LoadedCallback {
		public void onSaveHistory(final byte[] byteArray, final String imageUri, @NonNull final BatchAnnotateImagesResponse response) {
			final Realm realm = Realm.getDefaultInstance();
			realm.executeTransactionAsync(new Realm.Transaction() {
				@Override
				public void execute(Realm bgRealm) {
					HistoryItem historyItem = bgRealm.createObject(HistoryItem.class);
					historyItem.setImageUri(imageUri);
					historyItem.setByteArray(byteArray);
					historyItem.setSavedTime(System.currentTimeMillis());
					try {
						historyItem.setJsonText(response.toPrettyString());
					} catch (IOException e) {
						LL.d("Saved history fail.");
					}
				}
			}, new Realm.Transaction.OnSuccess() {
				@Override
				public void onSuccess() {
					LL.d("Saved history successfully.");
				}
			}, new Realm.Transaction.OnError() {
				@Override
				public void onError(Throwable error) {
					LL.d("Saved history fail.");
				}
			});
		}

		public void onVisionResponse(BatchAnnotateImagesResponse response) {
			if (response == null) {
				LL.d("response is NULL.");
				return;
			}
			try {
				LL.d("response is OK.");
				LL.d(response.toPrettyString());
			} catch (IOException e) {
				LL.d("response is available but IO problem: " + e.toString());
			}
		}

		public void onError(@NonNull Status status) {
			if (!TextUtils.isEmpty(status.getMessage())) {
				LL.e(status.getMessage());
			} else {
				LL.e("Vision API on some error:");
				final List<Map<String, Object>> details = status.getDetails();
				for (Map<String, Object> mapDetail : details) {
					Set<String> mapDetailKeys = mapDetail.keySet();
					for (String key : mapDetailKeys) {
						LL.e(mapDetail.get(key)
						              .toString());
					}
				}
			}
		}

		public void onException(@NonNull Exception e) {
			LL.e(e.toString());
		}

	}
}
