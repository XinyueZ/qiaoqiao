package com.qiaoqiao.vision.ui;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.api.services.vision.v1.model.EntityAnnotation;
import com.google.api.services.vision.v1.model.WebDetection;
import com.qiaoqiao.R;
import com.qiaoqiao.databinding.FragmentListVisionBinding;
import com.qiaoqiao.vision.model.VisionEntity;

public final class VisionListFragment extends Fragment {
	/**
	 * Main layout for this component.
	 */
	private static final int LAYOUT = R.layout.fragment_list_vision;
	private FragmentListVisionBinding mBinding;

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		mBinding = DataBindingUtil.inflate(inflater, LAYOUT, container, false);
		return mBinding.getRoot();
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		mBinding.visionRv.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
		mBinding.visionRv.setHasFixedSize(true);
		mBinding.visionRv.setAdapter(new VisionListAdapter());
	}

	public void addLandmarkEntity(@NonNull EntityAnnotation entityAnnotation) {
		VisionListAdapter adapter = (VisionListAdapter) mBinding.visionRv.getAdapter();
		adapter.addVisionEntity(new VisionEntity(entityAnnotation, "LANDMARK_DETECTION"));
	}

	public void addEntity(@NonNull WebDetection webDetection) {
		VisionListAdapter adapter = (VisionListAdapter) mBinding.visionRv.getAdapter();
		adapter.addVisionEntity(new VisionEntity(webDetection, "WEB_DETECTION"));

	}
}
