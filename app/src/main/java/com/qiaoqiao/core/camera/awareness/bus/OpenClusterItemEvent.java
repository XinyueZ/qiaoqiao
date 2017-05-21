package com.qiaoqiao.core.camera.awareness.bus;


import android.support.annotation.NonNull;

import com.google.maps.android.clustering.ClusterItem;

public final class OpenClusterItemEvent {
	private final @NonNull ClusterItem mClusterItem;

	public OpenClusterItemEvent(@NonNull ClusterItem clusterItem) {
		mClusterItem = clusterItem;
	}

	public @NonNull
	ClusterItem getClusterItem() {
		return mClusterItem;
	}
}
