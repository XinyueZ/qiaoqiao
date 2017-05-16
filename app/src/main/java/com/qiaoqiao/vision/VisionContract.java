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
import android.support.annotation.Nullable;

import com.google.api.services.vision.v1.model.BatchAnnotateImagesResponse;
import com.qiaoqiao.app.mvp.BasePresenter;
import com.qiaoqiao.app.mvp.BaseView;
import com.qiaoqiao.databinding.FragmentListVisionBinding;
import com.qiaoqiao.ds.DsLoadedCallback;
import com.qiaoqiao.ds.DsRepository;
import com.qiaoqiao.vision.model.VisionEntity;

import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * This specifies the contract between the view and the presenter.
 */
public interface VisionContract {

	interface View<T, S> extends BaseView<Presenter, FragmentListVisionBinding> {
		FragmentListVisionBinding getBinding();

		void addEntity(@Nullable T t, @Nullable S s);


		void showDetail(@NonNull VisionEntity entity, android.view.View transitionView);

		void clean();

		void setRefreshing(boolean refresh);
	}

	abstract class Presenter implements BasePresenter {

		protected abstract void addResponseToScreen(@NonNull BatchAnnotateImagesResponse response);

		protected final @NonNull DsRepository mDsRepository;

		public Presenter(@NonNull DsRepository dsRepository) {
			mDsRepository = dsRepository;
		}

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

		public void waitForImageUri(@NonNull List<VisionEntity> list) {

		}

		public abstract void setRefreshing(boolean refresh);

		public final void loadRecent() {
			mDsRepository.onRecentRequest(new DsLoadedCallback() {
				@Override
				public void onVisionResponse(BatchAnnotateImagesResponse response) {
					super.onVisionResponse(response);
					addResponseToScreen(response);
				}
			});
		}
	}
}
