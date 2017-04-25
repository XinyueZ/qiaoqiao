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

import com.qiaoqiao.backend.Service;
import com.qiaoqiao.backend.WikipediaAPIs;

import java.io.File;

import javax.inject.Singleton;


@Singleton
public final class DsRepository extends AbstractDsSource {
	private @NonNull  final AbstractDsSource mWebDs;
	private @NonNull  final AbstractDsSource mLocalDs;
	private @NonNull  final AbstractDsSource mCameraDs;
	private @NonNull final AbstractDsSource mWikipediaRemoteDs;

	DsRepository(@NonNull Service service,
	             @NonNull WikipediaAPIs wikipediaAPIs,
	             @Web AbstractDsSource webDs,
	             @Local AbstractDsSource localDs,
	             @Camera AbstractDsSource cameraDs,
	             @Wikipedia AbstractDsSource wikipediaRemoteDs) {
		super(service, wikipediaAPIs);
		mWebDs = webDs;
		mLocalDs = localDs;
		mCameraDs = cameraDs;
		mWikipediaRemoteDs = wikipediaRemoteDs;
	}

	@Override
	public void onKnowledgeQuery(@NonNull String keyword, @NonNull LoadedCallback callback) {
		mWikipediaRemoteDs.onKnowledgeQuery(keyword, callback);
	}

	@Override
	public void onBytes(@NonNull byte[] bytes, @NonNull LoadedCallback callback) {
		mCameraDs.onBytes(bytes, callback);
	}

	@Override
	public void onFile(@NonNull File file, @NonNull LoadedCallback callback) {
		mLocalDs.onFile(file, callback);
	}

	@Override
	public void onUri(@NonNull Uri uri, @NonNull LoadedCallback callback) {
		mWebDs.onUri(uri, callback);
	}
}
