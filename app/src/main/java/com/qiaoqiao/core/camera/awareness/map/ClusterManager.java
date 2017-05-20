/*
 * Copyright 2016 Google Inc. All Rights Reserved.
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

package com.qiaoqiao.core.camera.awareness.map;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.maps.GoogleMap;
import com.google.maps.android.MarkerManager;
import com.google.maps.android.clustering.ClusterItem;
import com.qiaoqiao.core.detail.ui.DetailActivity;

import java.lang.ref.WeakReference;
import java.util.List;


public final class ClusterManager extends com.google.maps.android.clustering.ClusterManager implements com.google.maps.android.clustering.ClusterManager.OnClusterItemClickListener<ClusterItem> {
	private final WeakReference<FragmentActivity> mActivityWeakReference;

	private ClusterManager(@NonNull FragmentActivity activity, @NonNull GoogleMap map) {
		super(activity.getApplicationContext(), map, new MarkerManager(map));
		map.setOnMarkerClickListener(this);
		setRenderer(new ClusterRenderer(activity.getApplicationContext(), map, this));
		getRenderer().setAnimation(true);
		setOnClusterItemClickListener(this);
		mActivityWeakReference = new WeakReference<>(activity);
	}

	public static ClusterManager showGeosearch(@NonNull FragmentActivity activity, @NonNull GoogleMap googleMap, @Nullable List<ClusterItem> clusterItems) {
		ClusterManager ret = new ClusterManager(activity, googleMap);
		ret.addItems(clusterItems);
		return ret;
	}


	@Override
	public boolean onClusterItemClick(ClusterItem clusterItem) {
		if (mActivityWeakReference.get() == null) {
			return true;
		}
		DetailActivity.showInstance(mActivityWeakReference.get(), Integer.valueOf(clusterItem.getSnippet()));
		return true;
	}
}
