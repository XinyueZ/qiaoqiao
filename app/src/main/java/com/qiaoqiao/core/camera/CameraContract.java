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

package com.qiaoqiao.core.camera;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.qiaoqiao.core.camera.barcode.FrameUpdateCallback;
import com.qiaoqiao.core.camera.crop.model.CropSource;
import com.qiaoqiao.core.camera.vision.model.VisionEntity;
import com.qiaoqiao.databinding.ActivityCameraBinding;
import com.qiaoqiao.mvp.BasePresenter;
import com.qiaoqiao.mvp.BaseView;

import java.io.IOException;

/**
 * This specifies the contract between the view and the presenter.
 */
public interface CameraContract {

	interface View extends BaseView<CameraPresenter, ActivityCameraBinding> {

		void showLoadFromLocal(@Nullable android.view.View v);

		void showInputFromWeb(@Nullable android.view.View v);

		void showError(@NonNull String errorMessage);

		void updateViewWhenResponse(VisionEntity visionEntity);

		void updateViewWhenRequest();

		ActivityCameraBinding getBinding();

		void openCrop(@NonNull CropSource cropSource);

		void tfOutput(String tfOutput);
	}

	interface Presenter extends BasePresenter,
	                            FrameUpdateCallback {

		void updateWhenResponse(VisionEntity visionEntity);

		void findAnnotateImages(@NonNull byte[] bytes);

		void capturedByteArray(@NonNull Context cxt, @NonNull byte[] bytes) throws IOException;
	}
}
