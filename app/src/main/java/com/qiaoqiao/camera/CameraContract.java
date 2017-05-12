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

package com.qiaoqiao.camera;


import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.google.android.cameraview.CameraView;
import com.google.api.services.vision.v1.model.BatchAnnotateImagesResponse;
import com.qiaoqiao.app.mvp.BasePresenter;
import com.qiaoqiao.app.mvp.BaseView;
import com.qiaoqiao.databinding.ActivityCameraBinding;

/**
 * This specifies the contract between the view and the presenter.
 */
public interface CameraContract {

	interface View extends BaseView<CameraPresenter, ActivityCameraBinding> {
		void showLoadFromLocal();

		void showInputFromWeb();

		void showError(@NonNull String errorMessage);

		void addResponseToScreen(@NonNull BatchAnnotateImagesResponse response);

		void hideSystemUi();

		void capturePhoto();

		void openLink();

		void openLocal();

		void showMainControl();

		void cameraBegin(@NonNull CameraView.Callback callback);

		void cameraEnd(@NonNull CameraView.Callback callback);

		void updateUIWhenGetPhoto();

		ActivityCameraBinding getBinding();
	}

	interface Presenter extends BasePresenter {
		void stop();


		void openLink(@NonNull Uri uri);

		void openLocal(@NonNull Context cxt, @NonNull Uri uri);
	}
}
