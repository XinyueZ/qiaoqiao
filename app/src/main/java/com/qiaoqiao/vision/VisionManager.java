package com.qiaoqiao.vision;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.content.res.AppCompatResources;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;

import com.google.api.services.vision.v1.model.AnnotateImageResponse;
import com.google.api.services.vision.v1.model.BatchAnnotateImagesResponse;
import com.google.api.services.vision.v1.model.EntityAnnotation;
import com.google.api.services.vision.v1.model.WebDetection;
import com.google.api.services.vision.v1.model.WebEntity;
import com.qiaoqiao.R;
import com.qiaoqiao.databinding.FragmentListVisionBinding;
import com.qiaoqiao.utils.DeviceUtils;
import com.qiaoqiao.utils.LL;
import com.qiaoqiao.vision.model.VisionEntity;
import com.qiaoqiao.vision.ui.VisionListAdapter;

import java.util.List;

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
		final Context context = mBinding.getFragment()
		                                .getContext();
		final DeviceUtils.ScreenSize screenSize = DeviceUtils.getScreenSize(context);
		mBinding.visionRv.getLayoutParams().width = (int) (screenSize.Width / 2f);
		mBinding.visionRv.setLayoutManager(new LinearLayoutManager(mBinding.getFragment()
		                                                                   .getActivity(), LinearLayoutManager.VERTICAL, true));
		mBinding.visionRv.setHasFixedSize(true);
		mBinding.visionRv.setAdapter(mVisionListAdapter = new VisionListAdapter());
		final DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(context, DividerItemDecoration.VERTICAL);
		dividerItemDecoration.setDrawable(AppCompatResources.getDrawable(context, R.drawable.divider_drawable));
		mBinding.visionRv.addItemDecoration(dividerItemDecoration);

	}

	@Override
	public void stop() {

	}

	@Override
	public void addResponseToScreen(@NonNull BatchAnnotateImagesResponse response) {
		final List<AnnotateImageResponse> annotates = response.getResponses();
		if (annotates != null && annotates.size() > 0) {
			final AnnotateImageResponse annotateImage = annotates.get(0);
			if (annotateImage != null) {
				final List<EntityAnnotation> landmarkAnnotations = annotateImage.getLandmarkAnnotations();
				if (landmarkAnnotations != null && landmarkAnnotations.size() > 0) {
					final EntityAnnotation entityAnnotation = landmarkAnnotations.get(0);
					if (entityAnnotation != null) {
						addLandmarkEntity(entityAnnotation);
					}
				}
				final WebDetection webDetection = annotateImage.getWebDetection();
				if (webDetection != null) {
					final List<WebEntity> webEntities = webDetection.getWebEntities();
					if (webEntities != null && webEntities.size() > 0) {
						final WebEntity webEntity = webEntities.get(0);
						if (webEntity != null) {
							addWebEntity(webEntity);
						}
					}
				}
			}
		}
	}


	private void addLandmarkEntity(@NonNull EntityAnnotation entityAnnotation) {
		LL.d("addLandmarkEntity");
		mVisionListAdapter.addVisionEntity(new VisionEntity(entityAnnotation, "LANDMARK_DETECTION"));
	}

	private void addWebEntity(@NonNull WebEntity webEntity) {
		LL.d("addWebEntity");
		mVisionListAdapter.addVisionEntity(new VisionEntity(webEntity, "WEB_DETECTION"));

	}
}
