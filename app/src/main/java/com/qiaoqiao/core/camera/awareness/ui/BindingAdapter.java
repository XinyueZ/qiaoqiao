package com.qiaoqiao.core.camera.awareness.ui;


import android.support.annotation.NonNull;
import android.widget.ImageView;

import com.qiaoqiao.core.camera.awareness.map.PlaceWrapper;

public final class BindingAdapter {
	@android.databinding.BindingAdapter({ "bind:placeWrapper" })
	public static void setPlaceWrapperImage(@NonNull ImageView imageView, @NonNull PlaceWrapper placeWrapper) {
		imageView.setImageBitmap(placeWrapper.getBitmap());
	}
}
