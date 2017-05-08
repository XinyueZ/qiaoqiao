package com.qiaoqiao.vision;


import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.content.res.AppCompatResources;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;

import com.google.android.gms.maps.model.LatLng;
import com.google.api.services.vision.v1.model.AnnotateImageResponse;
import com.google.api.services.vision.v1.model.BatchAnnotateImagesResponse;
import com.google.api.services.vision.v1.model.EntityAnnotation;
import com.google.api.services.vision.v1.model.WebDetection;
import com.google.api.services.vision.v1.model.WebEntity;
import com.qiaoqiao.R;
import com.qiaoqiao.detail.ui.DetailActivity;
import com.qiaoqiao.location.ui.MapActivity;
import com.qiaoqiao.vision.bus.VisionEntityClickEvent;
import com.qiaoqiao.vision.model.VisionEntity;
import com.qiaoqiao.vision.ui.VisionListAdapter;

import java.util.List;

import javax.inject.Inject;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;

public final class VisionManager implements VisionContract.Presenter {
	private final @NonNull VisionContract.View mView;
	private VisionListAdapter mVisionListAdapter;

	//------------------------------------------------
	//Subscribes, event-handlers
	//------------------------------------------------

	/**
	 * Handler for {@link VisionEntityClickEvent}.
	 *
	 * @param e Event {@link VisionEntityClickEvent}.
	 */
	@Subscribe
	public void onEvent(VisionEntityClickEvent e) {
		final VisionEntity entity = e.getEntity();
		final LatLng latLng = entity.getLocation()
		                            .toLatLng();
		if (TextUtils.equals("LANDMARK_DETECTION", entity.getReadableName()) && latLng != null) {
			MapActivity.showInstance(mView.getBinding()
			                              .getFragment()
			                              .getActivity(), latLng);
			return;
		}
		final String descriptionText = entity.getDescription()
		                                     .getDescriptionText();
		if (TextUtils.equals("WEB_DETECTION", entity.getReadableName()) && !TextUtils.isEmpty(descriptionText)) {
			DetailActivity.showInstance(mView.getBinding()
			                                 .getFragment()
			                                 .getActivity(), descriptionText);
		}
	}

	//------------------------------------------------


	@Inject
	VisionManager(@NonNull VisionContract.View view) {
		mView = view;
	}

	@Inject
	void onInjected() {
		mView.setPresenter(this);
	}

	@Override
	public void begin() {
		EventBus.getDefault()
		        .register(this);

		final Context context = mView.getBinding()
		                             .getFragment()
		                             .getContext();
//		final DeviceUtils.ScreenSize screenSize = DeviceUtils.getScreenSize(context);
//		mBinding.visionRv.getLayoutParams().width = (int) (screenSize.Width / 2f);
		mView.getBinding().visionRv.setLayoutManager(new LinearLayoutManager(mView.getBinding()
		                                                                          .getFragment()
		                                                                          .getActivity(), LinearLayoutManager.VERTICAL, false));
		mView.getBinding().visionRv.setHasFixedSize(true);
		mView.getBinding().visionRv.setAdapter(mVisionListAdapter = new VisionListAdapter());
		final DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(context, DividerItemDecoration.VERTICAL);
		final Drawable divideDrawable = AppCompatResources.getDrawable(context, R.drawable.divider_drawable);
		if (divideDrawable != null) {
			dividerItemDecoration.setDrawable(divideDrawable);
		}
		mView.getBinding().visionRv.addItemDecoration(dividerItemDecoration);

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
						mView.getBinding().visionRv.scrollToPosition(mVisionListAdapter.getItemCount() - 1);
					}
				}
				final WebDetection webDetection = annotateImage.getWebDetection();
				if (webDetection != null) {
					final List<WebEntity> webEntities = webDetection.getWebEntities();
					if (webEntities != null && webEntities.size() > 0) {
						final WebEntity webEntity = webEntities.get(0);
						if (webEntity != null) {
							addWebEntity(webEntity);
							mView.getBinding().visionRv.scrollToPosition(mVisionListAdapter.getItemCount() - 1);
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
