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

package com.qiaoqiao.home;


import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.google.api.services.vision.v1.model.BatchAnnotateImagesResponse;
import com.qiaoqiao.app.mvp.BasePresenter;
import com.qiaoqiao.app.mvp.BaseView;
import com.qiaoqiao.databinding.HomeBinding;

/**
 * This specifies the contract between the view and the presenter.
 */
public interface HomeContract {

	interface View extends BaseView<Home, HomeBinding> {
		void showLoadFromLocal();

		void showInputFromWeb();

		void showError(@NonNull android.view.View view, @NonNull String errorMessage);

		void addResponseToScreen(@NonNull BatchAnnotateImagesResponse response);

		HomeBinding getBinding();
	}

	interface Presenter extends BasePresenter {
		void stop();


		void capturePhoto();

		void openLink(@NonNull Uri uri);

		void openLocal(@NonNull Context cxt, @NonNull Uri uri);
	}
}
