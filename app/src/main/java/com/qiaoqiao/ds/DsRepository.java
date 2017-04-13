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
import com.qiaoqiao.keymanager.Key;

import java.io.File;

import javax.inject.Singleton;


@Singleton
public final class DsRepository extends AbstractDsSource {
	private final AbstractDsSource mWebDs;
	private final AbstractDsSource mLocalDs;
	private final AbstractDsSource mCameraDs;

	DsRepository(@NonNull Service service, @NonNull Key key, @Web AbstractDsSource webDs, @Local AbstractDsSource localDs, @Camera AbstractDsSource cameraDs) {
		super(service, key);
		mWebDs = webDs;
		mLocalDs = localDs;
		mCameraDs = cameraDs;
	}

	@Override
	public void captureCamera(@NonNull byte[] bytes, @NonNull BytesLoadedCallback callback) {
		mCameraDs.captureCamera(bytes, callback);
	}

	@Override
	public void readLocal(@NonNull File file, BytesLoadedCallback callback) {
		mLocalDs.readLocal(file, callback);
	}

	@Override
	public void openWebLink(@NonNull Uri uri, OpenWebLinkCallback callback) {
		mWebDs.openWebLink(uri, callback);
	}
}
