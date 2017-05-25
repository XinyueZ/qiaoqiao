package com.qiaoqiao.core.camera.vision;


import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.google.api.services.vision.v1.model.AnnotateImageResponse;
import com.google.api.services.vision.v1.model.BatchAnnotateImagesResponse;
import com.google.api.services.vision.v1.model.EntityAnnotation;
import com.google.api.services.vision.v1.model.WebDetection;
import com.google.api.services.vision.v1.model.WebEntity;
import com.qiaoqiao.core.camera.vision.annotation.target.Single;
import com.qiaoqiao.core.camera.vision.bus.VisionEntityClickEvent;
import com.qiaoqiao.core.camera.vision.model.VisionEntity;
import com.qiaoqiao.repository.DsRepository;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import de.greenrobot.event.Subscribe;

public final class VisionPresenter extends VisionContract.Presenter {
	private final @NonNull VisionContract.View mView;


	//------------------------------------------------
	//Subscribes, event-handlers
	//------------------------------------------------

	/**
	 * Handler for {@link VisionEntityClickEvent}.
	 *
	 * @param e Event {@link VisionEntityClickEvent}.
	 */
	@SuppressWarnings("unused")
	@Subscribe
	public void onEvent(VisionEntityClickEvent e) {
		mView.showDetail(e.getEntity(), e.getTransitionView());

	}


	//------------------------------------------------


	@Inject
	VisionPresenter(@NonNull @Single VisionContract.View view, @NonNull DsRepository dsRepository) {
		super(dsRepository);
		mView = view;
	}

	@Inject
	void onInjected() {
		mView.setPresenter(this);
	}


	@Override
	public void addResponseToScreen(@NonNull BatchAnnotateImagesResponse response) {
		final List<AnnotateImageResponse> annotates = response.getResponses();
		List<VisionEntity> filterList = new ArrayList<>();
		if (annotates != null && annotates.size() > 0) {
			final AnnotateImageResponse annotateImage = annotates.get(0);
			if (annotateImage != null) {
				final List<EntityAnnotation> labelAnnotations = annotateImage.getLabelAnnotations();
				if (labelAnnotations != null && labelAnnotations.size() > 0) {
					for (EntityAnnotation annotation : labelAnnotations) {
						if (!TextUtils.isEmpty(annotation.getDescription()) && annotation.getScore() >= 0.7) {
							filterList.add(new VisionEntity(annotation, "LABEL_DETECTION").setActivated(true));
						}
					}
				}

				final List<EntityAnnotation> landmarkAnnotations = annotateImage.getLandmarkAnnotations();
				if (landmarkAnnotations != null && landmarkAnnotations.size() > 0) {
					for (EntityAnnotation annotation : landmarkAnnotations) {
						if (!TextUtils.isEmpty(annotation.getDescription()) && annotation.getScore() >= 0.7) {
							filterList.add(new VisionEntity(annotation, "LANDMARK_DETECTION").setActivated(true));
						}
					}
				}
				final List<EntityAnnotation> logoAnnotations = annotateImage.getLogoAnnotations();
				if (logoAnnotations != null && logoAnnotations.size() > 0) {
					for (EntityAnnotation annotation : logoAnnotations) {
						if (!TextUtils.isEmpty(annotation.getDescription()) && annotation.getScore() >= 0.7) {
							filterList.add(new VisionEntity(annotation, "LOGO_DETECTION").setActivated(true));
						}
					}
				}

				final WebDetection webDetection = annotateImage.getWebDetection();
				if (webDetection != null) {
					final List<WebEntity> webEntities = webDetection.getWebEntities();
					if (webEntities != null && webEntities.size() > 0) {
						for (WebEntity entity : webEntities) {
							if (!TextUtils.isEmpty(entity.getDescription()) && entity.getScore() >= 0.7) {
								filterList.add(new VisionEntity(entity, "WEB_DETECTION").setActivated(true));
							}
						}
					}
				}
			}
		}
		mView.addEntities(filterList);
	}

	@Override
	public void setRefreshing(boolean refresh) {
		mView.setRefreshing(refresh);
	}
}
