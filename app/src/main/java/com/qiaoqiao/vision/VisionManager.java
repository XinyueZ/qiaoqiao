package com.qiaoqiao.vision;


import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;

import com.google.api.services.vision.v1.model.BatchAnnotateImagesResponse;
import com.google.api.services.vision.v1.model.EntityAnnotation;
import com.google.api.services.vision.v1.model.WebDetection;
import com.qiaoqiao.databinding.FragmentListVisionBinding;
import com.qiaoqiao.vision.model.VisionEntity;
import com.qiaoqiao.vision.ui.VisionListAdapter;

import javax.inject.Inject;

public final class VisionManager implements VisionContract.Presenter {
	private final @NonNull FragmentListVisionBinding mBinding;
	private VisionListAdapter mVisionListAdapter;
	private @NonNull VisionContract.View mView;

	@Inject
	VisionManager(@NonNull FragmentListVisionBinding binding, @NonNull VisionContract.View view) {
		mBinding = binding;
		mView = view;
	}

	@Inject
	void onInjected() {
		mView.setPresenter(this);
	}

	@Override
	public void start() {
		mBinding.visionRv.setLayoutManager(new LinearLayoutManager(mBinding.getFragment()
		                                                                   .getActivity(), LinearLayoutManager.VERTICAL, false));
		mBinding.visionRv.setHasFixedSize(true);
		mBinding.visionRv.setAdapter(mVisionListAdapter = new VisionListAdapter());
	}

	@Override
	public void stop() {

	}

	@Override
	public void addResponseToScreen(@NonNull BatchAnnotateImagesResponse response) {

	}


	private void addLandmarkEntity(@NonNull EntityAnnotation entityAnnotation) {
		mVisionListAdapter.addVisionEntity(new VisionEntity(entityAnnotation, "LANDMARK_DETECTION"));
	}

	private void addWebEntity(@NonNull WebDetection webDetection) {
		mVisionListAdapter.addVisionEntity(new VisionEntity(webDetection, "WEB_DETECTION"));

	}
}
