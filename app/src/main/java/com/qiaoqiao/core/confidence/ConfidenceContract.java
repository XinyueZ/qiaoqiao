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

package com.qiaoqiao.core.confidence;


import android.content.Context;
import android.support.annotation.NonNull;

import com.qiaoqiao.core.confidence.ui.Confidence;
import com.qiaoqiao.databinding.FragmentConfidenceDialogBinding;
import com.qiaoqiao.mvp.BasePresenter;
import com.qiaoqiao.mvp.BaseView;

/**
 * This specifies the contract between the view and the presenter.
 */
public interface ConfidenceContract {
	interface View extends BaseView<ConfidencePresenter, FragmentConfidenceDialogBinding> {
		FragmentConfidenceDialogBinding getBinding();

		void showLabelConfidence(@NonNull Confidence confidence);

		void showLogoConfidence(@NonNull Confidence confidence);

		void showImageConfidence(@NonNull Confidence confidence);
	}


	interface Presenter extends BasePresenter {

		void loadAllConfidences(@NonNull Context cxt);

		@NonNull
		Presenter save(@NonNull Context context, @NonNull Confidence onSaveItem);
	}
}
