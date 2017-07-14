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

package com.qiaoqiao.core.camera.vision;


import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;

import com.google.api.services.vision.v1.model.BatchAnnotateImagesResponse;
import com.qiaoqiao.core.camera.vision.model.VisionEntity;
import com.qiaoqiao.databinding.FragmentListVisionBinding;
import com.qiaoqiao.mvp.BasePresenter;
import com.qiaoqiao.mvp.BaseView;
import com.qiaoqiao.repository.DsLoadedCallback;
import com.qiaoqiao.repository.DsRepository;

import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * This specifies the contract between the view and the presenter.
 */
public interface VisionContract {

	interface View extends BaseView<Presenter, FragmentListVisionBinding> {
		FragmentListVisionBinding getBinding();

		void clear();

		void addEntities(@NonNull List<VisionEntity> visionEntityList);

		void showDetail(@NonNull VisionEntity entity, android.view.View transitionView);

		void setRefreshing(boolean refresh);
	}

	abstract class Presenter implements BasePresenter {
		public abstract void clear();

		public abstract void addResponseToScreen(@NonNull BatchAnnotateImagesResponse response, boolean show);

		protected final @NonNull DsRepository mDsRepository;

		public Presenter(@NonNull DsRepository dsRepository) {
			mDsRepository = dsRepository;
		}

		@Override
		public void begin(@NonNull FragmentActivity hostActivity) {
			EventBus.getDefault()
			        .register(this);

		}

		@Override
		public void end(@NonNull FragmentActivity hostActivity) {
			EventBus.getDefault()
			        .unregister(this);
		}


		public abstract void setRefreshing(boolean refresh);

		public final void loadRecent() {
			mDsRepository.onRecentRequest(new DsLoadedCallback() {
				@Override
				public void onVisionResponse(BatchAnnotateImagesResponse response) {
					super.onVisionResponse(response);
					addResponseToScreen(response, false);
				}
			});
		}
	}
}
