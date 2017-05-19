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

package com.qiaoqiao.repository;

import android.net.Uri;
import android.support.annotation.NonNull;

import com.google.android.gms.maps.model.LatLng;
import com.qiaoqiao.repository.annotation.RepositoryScope;
import com.qiaoqiao.repository.annotation.target.Camera;
import com.qiaoqiao.repository.annotation.target.Database;
import com.qiaoqiao.repository.annotation.target.Knowledge;
import com.qiaoqiao.repository.annotation.target.Local;
import com.qiaoqiao.repository.annotation.target.Web;
import com.qiaoqiao.repository.backend.Google;
import com.qiaoqiao.repository.backend.model.wikipedia.LangLink;

import java.io.File;


@RepositoryScope
public final class DsRepository extends AbstractDsSource {
	private @NonNull  final AbstractDsSource mWebDs;
	private @NonNull  final AbstractDsSource mLocalDs;
	private @NonNull  final AbstractDsSource mCameraDs;
	private @NonNull  final AbstractDsSource mKnowledgeRemoteDs;
	private @NonNull  final AbstractDsSource mDatabaseDs;

	DsRepository(@NonNull Google google,
	             @NonNull com.qiaoqiao.repository.backend.Wikipedia wikipedia,
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
	public void onGeosearchQuery(@NonNull LatLng latLng, @NonNull DsLoadedCallback callback) {
		mKnowledgeRemoteDs.onGeosearchQuery(latLng, callback);
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
