package com.qiaoqiao.vision.ui;


import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.TextUtils;

import com.google.android.gms.maps.model.LatLng;
import com.qiaoqiao.detail.ui.DetailActivity;
import com.qiaoqiao.location.ui.MapActivity;
import com.qiaoqiao.vision.model.VisionEntity;

public  abstract class AbstractVisionFragment extends Fragment {



	protected void openDetail(@NonNull VisionEntity entity) {
		Activity activity = getActivity();
		if (activity == null) {
			return;
		}
		final LatLng latLng = entity.getLocation()
		                            .toLatLng();
		if (TextUtils.equals("LANDMARK_DETECTION", entity.getReadableName()) && latLng != null) {
			MapActivity.showInstance(activity, latLng);
			return;
		}
		final String descriptionText = entity.getDescription()
		                                     .getDescriptionText();
		if (TextUtils.equals("WEB_DETECTION", entity.getReadableName()) && !TextUtils.isEmpty(descriptionText)) {
			DetailActivity.showInstance(activity, descriptionText);
		}
	}
}
