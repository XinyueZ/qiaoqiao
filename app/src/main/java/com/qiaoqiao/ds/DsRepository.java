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

import android.content.Context;
import android.support.annotation.NonNull;

import javax.inject.Inject;
import javax.inject.Singleton;


@Singleton
public final class DsRepository implements DsSource {
	private final DsSource mWebDs;
	private final DsSource mLocalDs;
	private final DsSource mCameraDs;

	@Inject
	DsRepository(  @Web DsSource webDs, @Local DsSource localDs, @Camera DsSource cameraDs) {
		mWebDs = webDs;
		mLocalDs = localDs;
		mCameraDs = cameraDs;
	}

	@Override
	public void loadData(@NonNull Context cxt, @DsType int dsType, @NonNull DataLoadedCallback callback) {
		mWebDs.loadData(cxt, dsType, callback);
		mLocalDs.loadData(cxt, dsType, callback);
		mCameraDs.loadData(cxt, dsType, callback);
	}
}
