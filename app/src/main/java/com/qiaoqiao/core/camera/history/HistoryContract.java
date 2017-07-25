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

package com.qiaoqiao.core.camera.history;


import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.qiaoqiao.core.camera.crop.CropPresenter;
import com.qiaoqiao.databinding.FragmentStackviewHistoryBinding;
import com.qiaoqiao.mvp.BasePresenter;
import com.qiaoqiao.mvp.BaseView;
import com.qiaoqiao.repository.database.HistoryItem;

import java.util.List;

/**
 * This specifies the contract between the view and the presenter.
 */
public interface HistoryContract {

	interface View2 extends BaseView<HistoryPresenter2, FragmentStackviewHistoryBinding> {
		FragmentStackviewHistoryBinding getBinding();

		void showList(@NonNull List<HistoryItem> results);

		void updateList(@NonNull List<HistoryItem> historyItemList);

	}


	interface Presenter2 extends BasePresenter {
		void loadHistory();

		void setCropPresenter(@Nullable CropPresenter cropPresenter);
	}
}
