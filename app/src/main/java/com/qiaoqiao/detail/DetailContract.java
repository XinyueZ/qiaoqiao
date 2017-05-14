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

package com.qiaoqiao.detail;


import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.qiaoqiao.app.mvp.BasePresenter;
import com.qiaoqiao.app.mvp.BaseView;
import com.qiaoqiao.backend.model.wikipedia.Image;
import com.qiaoqiao.backend.model.wikipedia.LangLink;
import com.qiaoqiao.databinding.FragmentDetailBinding;

public interface DetailContract {

	interface View extends BaseView<DetailPresenter, FragmentDetailBinding> {
		FragmentDetailBinding getBinding();
		void showImage(@Nullable Image preview, @Nullable Image  photo);
		void setText(@NonNull String title, @NonNull String content);
		void setMultiLanguage(@Nullable LangLink[] langLinks);
	}

	interface Presenter extends BasePresenter {


		void loadDetail(LangLink langLink);
	}
}
