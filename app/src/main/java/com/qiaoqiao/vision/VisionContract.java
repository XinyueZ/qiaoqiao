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

package com.qiaoqiao.vision;


import android.support.annotation.NonNull;

import com.google.api.services.vision.v1.model.BatchAnnotateImagesResponse;
import com.qiaoqiao.app.mvp.BasePresenter;
import com.qiaoqiao.app.mvp.BaseView;
import com.qiaoqiao.databinding.FragmentListVisionBinding;
import com.qiaoqiao.vision.model.VisionEntity;

import de.greenrobot.event.EventBus;

/**
 * This specifies the contract between the view and the presenter.
 */
public interface VisionContract {

	interface View<T, S> extends BaseView<Presenter, FragmentListVisionBinding> {
		FragmentListVisionBinding getBinding();

		void addLandmarkEntity(@NonNull T t);

		void addWebEntity(@NonNull S s);

		void showDetail(@NonNull VisionEntity entity);
	}

	abstract class Presenter implements BasePresenter {
		protected abstract void addResponseToScreen(@NonNull BatchAnnotateImagesResponse response);

		@Override
		public void begin() {
			EventBus.getDefault()
			        .register(this);

		}

		@Override
		public void end() {
			EventBus.getDefault()
			        .unregister(this);
		}
	}
}
