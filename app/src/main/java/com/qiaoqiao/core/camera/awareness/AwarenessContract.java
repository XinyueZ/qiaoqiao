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

package com.qiaoqiao.core.camera.awareness;


import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;
import com.qiaoqiao.core.camera.awareness.ui.Adjust;
import com.qiaoqiao.databinding.PlacesBinding;
import com.qiaoqiao.mvp.BasePresenter;
import com.qiaoqiao.mvp.BaseView;

import java.util.List;

/**
 * This specifies the contract between the view and the presenter.
 */
public interface AwarenessContract {

	interface View extends BaseView<Presenter, PlacesBinding> {
		PlacesBinding getBinding();

		void onLocatingError();

		void onLocated(@NonNull LatLng latLng);

		void onPlayServiceConnectionFailed();

		void locating();


		void showAllGeoAndPlaces(@NonNull List<ClusterItem> clusterItemList);

		void showAdjust(@NonNull  Adjust adjust);
	}

	interface Presenter extends BasePresenter {
		void locating(@NonNull Context cxt);

		void settingLocating(@NonNull Activity cxt);

		void searchAndSearch(@NonNull Context cxt, @NonNull LatLng latLng);

		void loadGeosearchAdjust(@NonNull  Context cxt);
	}
}
