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

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.v7.content.res.AppCompatResources;
import android.view.LayoutInflater;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterItem;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import com.google.maps.android.ui.IconGenerator;
import com.qiaoqiao.R;
import com.qiaoqiao.databinding.LayoutPlaceBinding;
import com.qiaoqiao.repository.backend.model.wikipedia.geo.Geosearch;


/**
 * Custom renderer to use the app's styled markers.
 */
final class ClusterRenderer extends DefaultClusterRenderer<ClusterItem> {
	private final Context mContext;
	private final LayoutPlaceBinding mItemBinding;
	private final int mDimension;

	ClusterRenderer(@NonNull Context cxt, @NonNull GoogleMap map, @NonNull ClusterManager<ClusterItem> clusterManager) {
		super(cxt, map, clusterManager);
		mContext = cxt;

		LayoutInflater layoutInflater = (LayoutInflater) cxt.getSystemService(Context.LAYOUT_INFLATER_SERVICE);


		mItemBinding = LayoutPlaceBinding.inflate(layoutInflater);
		mItemBinding.setIconGenerator(new IconGenerator(cxt));
		mItemBinding.getIconGenerator()
		            .setContentView(mItemBinding.getRoot());

		mDimension = (int) cxt.getResources()
		                      .getDimension(R.dimen.custom_profile_image);
	}

	@Override
	protected boolean shouldRenderAsCluster(Cluster<ClusterItem> cluster) {
		return cluster.getSize() < 0;
	}

	@Override
	protected void onBeforeClusterItemRendered(ClusterItem clusterItem, MarkerOptions options) {
		if (clusterItem == null || options == null) {
			return;
		}
		if (clusterItem instanceof Geosearch) {
			options.position(clusterItem.getPosition())
			       .icon(getBitmapDescriptor(R.drawable.ic_geosearch));

		} else {
			mItemBinding.image.setImageBitmap(((PlaceWrapper) clusterItem).getBitmap());
			options.position(clusterItem.getPosition())
			       .icon(BitmapDescriptorFactory.fromBitmap(mItemBinding.getIconGenerator()
			                                                            .makeIcon()));
		}
	}

	private BitmapDescriptor getBitmapDescriptor(@DrawableRes int resId) {
		if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
			return BitmapDescriptorFactory.fromResource(resId);
		}
		Drawable drawable = AppCompatResources.getDrawable(mContext, resId);
		Canvas canvas = new Canvas();
		Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
		canvas.setBitmap(bitmap);
		drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
		drawable.draw(canvas);
		return BitmapDescriptorFactory.fromBitmap(bitmap);
	}
}
