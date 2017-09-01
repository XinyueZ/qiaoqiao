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

package com.qiaoqiao.core.detail;


import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.qiaoqiao.databinding.FragmentDetailBinding;
import com.qiaoqiao.mvp.BasePresenter;
import com.qiaoqiao.mvp.BaseView;
import com.qiaoqiao.repository.backend.model.wikipedia.Image;
import com.qiaoqiao.repository.backend.model.wikipedia.LangLink;

public interface DetailContract {

	interface View extends BaseView<DetailPresenter, FragmentDetailBinding> {
		FragmentDetailBinding getBinding();

		void setDetailImages(@Nullable Image preview, @Nullable Image photo);

		void setText(@NonNull String title, @NonNull String content);

		void loadDetail();

		void setMultiLanguage(@Nullable LangLink[] langLinks);

		void onError();
	}

	interface Presenter extends BasePresenter {

		void loadDetail(int pageId);

		void loadDetail(String text);

		void loadDetail(LangLink langLink);
	}
}
