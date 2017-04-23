package com.qiaoqiao.vision;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
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
import com.qiaoqiao.vision.bus.VisionEntityClickEvent;
import com.qiaoqiao.vision.model.VisionEntity;
import com.qiaoqiao.vision.ui.VisionListAdapter;

import java.util.List;

import javax.inject.Inject;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;

public final class VisionManager implements VisionContract.Presenter {
	private final @NonNull FragmentListVisionBinding mBinding;
	private final @NonNull VisionContract.View mView;
	private VisionListAdapter mVisionListAdapter;

	//------------------------------------------------
	//Subscribes, event-handlers
	//------------------------------------------------

	/**
	 * Handler for {@link VisionEntityClickEvent}.
	 * @param e Event {@link VisionEntityClickEvent}.
	 */
	@Subscribe
	public void onEvent(VisionEntityClickEvent e) {
		Snackbar.make(mBinding.getRoot(), e.getEntity().toString(), Snackbar.LENGTH_SHORT).show();
	}

	//------------------------------------------------


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
		EventBus.getDefault()
		        .register(this);

		final Context context = mBinding.getFragment()
		                                .getContext();
//		final DeviceUtils.ScreenSize screenSize = DeviceUtils.getScreenSize(context);
//		mBinding.visionRv.getLayoutParams().width = (int) (screenSize.Width / 2f);
		mBinding.visionRv.setLayoutManager(new LinearLayoutManager(mBinding.getFragment()
		                                                                   .getActivity(), LinearLayoutManager.VERTICAL, false));
		mBinding.visionRv.setHasFixedSize(true);
		mBinding.visionRv.setAdapter(mVisionListAdapter = new VisionListAdapter());
		final DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(context, DividerItemDecoration.VERTICAL);
		dividerItemDecoration.setDrawable(AppCompatResources.getDrawable(context, R.drawable.divider_drawable));
		mBinding.visionRv.addItemDecoration(dividerItemDecoration);

	}

	@Override
	public void stop() {
		EventBus.getDefault()
		        .unregister(this);
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
						mBinding.visionRv.scrollToPosition(mVisionListAdapter.getItemCount() - 1);
					}
				}
				final WebDetection webDetection = annotateImage.getWebDetection();
				if (webDetection != null) {
					final List<WebEntity> webEntities = webDetection.getWebEntities();
					if (webEntities != null && webEntities.size() > 0) {
						final WebEntity webEntity = webEntities.get(0);
						if (webEntity != null) {
							addWebEntity(webEntity);
							mBinding.visionRv.scrollToPosition(mVisionListAdapter.getItemCount() - 1);
						}
					}
				}
			}
		}
	}


	private void addLandmarkEntity(@NonNull EntityAnnotation entityAnnotation) {
		mVisionListAdapter.addVisionEntity(new VisionEntity(entityAnnotation, "LANDMARK_DETECTION"));
	}

	private void addWebEntity(@NonNull WebEntity webEntity) {
		mVisionListAdapter.addVisionEntity(new VisionEntity(webEntity, "WEB_DETECTION"));

	}
}
