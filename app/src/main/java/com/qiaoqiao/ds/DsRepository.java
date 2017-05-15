/*
 * Copyright 2016, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.qiaoqiao.ds;

import android.net.Uri;
import android.support.annotation.NonNull;

import com.qiaoqiao.backend.Google;
import com.qiaoqiao.backend.model.wikipedia.LangLink;
import com.qiaoqiao.ds.annotation.Camera;
import com.qiaoqiao.ds.annotation.Database;
import com.qiaoqiao.ds.annotation.Knowledge;
import com.qiaoqiao.ds.annotation.Local;
import com.qiaoqiao.ds.annotation.Web;
import com.qiaoqiao.vision.model.VisionEntity;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.inject.Singleton;


@Singleton
public final class DsRepository extends AbstractDsSource {
	private @NonNull  final AbstractDsSource mWebDs;
	private @NonNull  final AbstractDsSource mLocalDs;
	private @NonNull  final AbstractDsSource mCameraDs;
	private @NonNull  final AbstractDsSource mKnowledgeRemoteDs;
	private @NonNull  final AbstractDsSource mDatabaseDs;

	DsRepository(@NonNull Google google,
	             @NonNull com.qiaoqiao.backend.Wikipedia wikipedia,
	             @Web AbstractDsSource webDs,
	             @Local AbstractDsSource localDs,
	             @Camera AbstractDsSource cameraDs,
	             @Knowledge AbstractDsSource knowledgeRemoteDs,
	             @Database AbstractDsSource databaseDs) {
		super(google, wikipedia);
		mWebDs = webDs;
		mLocalDs = localDs;
		mCameraDs = cameraDs;
		mKnowledgeRemoteDs = knowledgeRemoteDs;
		mDatabaseDs = databaseDs;
	}

	@Override
	public void onKnowledgeQuery(@NonNull String keyword, @NonNull DsLoadedCallback callback) {
		mKnowledgeRemoteDs.onKnowledgeQuery(keyword, callback);
	}


	@Override
	public void onKnowledgeQuery(@NonNull LangLink langLink, @NonNull DsLoadedCallback callback) {
		mKnowledgeRemoteDs.onKnowledgeQuery(langLink, callback);
	}

	@Override
	public void onKnowledgeQuery(@NonNull List<VisionEntity> list) throws IOException {
		mKnowledgeRemoteDs.onKnowledgeQuery(list);
	}

	@Override
	public void onBytes(@NonNull byte[] bytes, @NonNull DsLoadedCallback callback) {
		mCameraDs.onBytes(bytes, callback);
	}

	@Override
	public void onFile(@NonNull File file, @NonNull DsLoadedCallback callback) {
		mLocalDs.onFile(file, callback);
	}

	@Override
	public void onUri(@NonNull Uri uri, @NonNull DsLoadedCallback callback) {
		mWebDs.onUri(uri, callback);
	}

	@Override
	public void onTranslate(@NonNull String q, @NonNull DsLoadedCallback callback) {
		mKnowledgeRemoteDs.onKnowledgeQuery(q, callback);
	}

	@Override
	public void onRecentRequest(@NonNull DsLoadedCallback callback) {
		mDatabaseDs.onRecentRequest(callback);
	}
}
