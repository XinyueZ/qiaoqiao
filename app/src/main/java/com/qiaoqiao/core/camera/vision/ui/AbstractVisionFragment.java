package com.qiaoqiao.core.camera.vision.ui;


import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.View;

import com.google.android.gms.maps.model.LatLng;
import com.qiaoqiao.core.detail.ui.DetailActivity;
import com.qiaoqiao.core.location.ui.MapActivity;
import com.qiaoqiao.core.camera.vision.model.VisionEntity;

public abstract class AbstractVisionFragment extends Fragment {

	protected void openDetail(@NonNull VisionEntity entity, @NonNull View transitionView) {
		if (!getUserVisibleHint()) {
			return;
		}
		Activity activity = getActivity();
		if (activity == null) {
			return;
		}
		final LatLng latLng = entity.getLocation()
		                            .toLatLng();
		if (TextUtils.equals("LANDMARK_DETECTION", entity.getReadableName()) && latLng != null) {
			MapActivity.showInstance(activity, latLng, transitionView);
			return;
		}
		final String descriptionText = entity.getDescription()
		                                     .getDescriptionText();
		if ((TextUtils.equals("WEB_DETECTION", entity.getReadableName()) || TextUtils.equals("LOGO_DETECTION", entity.getReadableName()) || TextUtils.equals("LABEL_DETECTION",
		                                                                                                                                                     entity.getReadableName())) && !TextUtils
				.isEmpty(
				descriptionText)) {
			DetailActivity.showInstance(activity, descriptionText, transitionView);
		}
	}
}