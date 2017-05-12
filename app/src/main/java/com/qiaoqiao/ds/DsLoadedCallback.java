package com.qiaoqiao.ds;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.google.api.services.vision.v1.model.BatchAnnotateImagesResponse;
import com.google.api.services.vision.v1.model.Status;
import com.qiaoqiao.backend.model.translate.Data;
import com.qiaoqiao.backend.model.wikipedia.WikiResult;
import com.qiaoqiao.ds.database.HistoryItem;
import com.qiaoqiao.utils.LL;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import io.realm.Realm;


public abstract class DsLoadedCallback {
	public void onSaveHistory(@Nullable  final byte[] byteArray, @Nullable  final Uri imageUri, @NonNull final BatchAnnotateImagesResponse response) {
		final Realm realm = Realm.getDefaultInstance();
		realm.executeTransactionAsync(bgRealm -> {
			HistoryItem historyItem = bgRealm.createObject(HistoryItem.class);
			if (imageUri != null) {
				historyItem.setImageUri(imageUri.toString());
			}
			historyItem.setByteArray(byteArray);
			historyItem.setSavedTime(System.currentTimeMillis());
			try {
				historyItem.setJsonText(response.toPrettyString());
			} catch (IOException e) {
				LL.d("Saved history fail.");
			}
		}, () -> LL.d("Saved history successfully."), error -> LL.d("Saved history fail."));
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

	public void onKnowledgeResponse(WikiResult result) {
		LL.d("response of wiki: " + result.toString());
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

	public void onTranslateData(@NonNull Data translateData) {
		LL.d("size of responses of cloud translate: " + translateData.getTranslations().length);
	}
}
