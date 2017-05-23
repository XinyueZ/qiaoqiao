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
import com.qiaoqiao.core.camera.awareness.bus.OpenClusterItemEvent;

import java.util.List;

import de.greenrobot.event.EventBus;


public final class  ClusterManager extends com.google.maps.android.clustering.ClusterManager implements com.google.maps.android.clustering.ClusterManager.OnClusterItemClickListener<ClusterItem> {

	private ClusterManager(@NonNull FragmentActivity activity, @NonNull GoogleMap map) {
		super(activity.getApplicationContext(), map, new MarkerManager(map));
		map.setOnMarkerClickListener(this);
		setRenderer(new ClusterRenderer(activity.getApplicationContext(), map, this));
		getRenderer().setAnimation(true);
		setOnClusterItemClickListener(this);
	}

	public static @Nullable
	ClusterManager showGeosearch(@Nullable FragmentActivity activity, @NonNull GoogleMap googleMap, @Nullable List<ClusterItem> clusterItems) {
		if (activity != null) {
			ClusterManager ret = new ClusterManager(activity, googleMap);
			ret.addItems(clusterItems);
			return ret;
		}
		return null;
	}


	@Override
	public boolean onClusterItemClick(ClusterItem clusterItem) {
		EventBus.getDefault().post(new OpenClusterItemEvent(clusterItem));
		return true;
	}
}
