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


import android.content.Context;
import android.support.annotation.NonNull;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.model.LatLng;
import com.qiaoqiao.core.camera.awareness.map.PlaceWrapper;
import com.qiaoqiao.databinding.PlacesBinding;
import com.qiaoqiao.mvp.BasePresenter;
import com.qiaoqiao.mvp.BaseView;
import com.qiaoqiao.repository.backend.model.wikipedia.geo.GeoResult;

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

		void solveSettingLocatingDialogProblem(@NonNull Status status);

		void showGeosearch(@NonNull GeoResult geoResult);

		void showPlaces(@NonNull List<PlaceWrapper> placeWrappers);
	}

	interface Presenter extends BasePresenter {
		void locating(@NonNull Context cxt);

		void settingLocating();

		void geosearch(@NonNull LatLng latLng);
	}
}
