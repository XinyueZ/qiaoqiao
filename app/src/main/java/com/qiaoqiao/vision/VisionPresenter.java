package com.qiaoqiao.vision;


import android.support.annotation.NonNull;

import com.google.api.services.vision.v1.model.AnnotateImageResponse;
import com.google.api.services.vision.v1.model.BatchAnnotateImagesResponse;
import com.google.api.services.vision.v1.model.EntityAnnotation;
import com.google.api.services.vision.v1.model.WebDetection;
import com.google.api.services.vision.v1.model.WebEntity;
import com.qiaoqiao.ds.DsRepository;
import com.qiaoqiao.vision.annotation.target.Single;
import com.qiaoqiao.vision.bus.VisionEntityClickEvent;

import java.util.List;

import javax.inject.Inject;

import de.greenrobot.event.Subscribe;

public final class VisionPresenter extends VisionContract.Presenter {
	private final @NonNull VisionContract.View<EntityAnnotation, EntityAnnotation, EntityAnnotation, WebEntity> mView;


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
		if (annotates != null && annotates.size() > 0) {
			final AnnotateImageResponse annotateImage = annotates.get(0);
			if (annotateImage != null) {
				EntityAnnotation landAnnotation = null;
				EntityAnnotation logoAnnotation = null;
				EntityAnnotation labelAnnotation = null;
				WebEntity webEntity = null;
				final List<EntityAnnotation> landmarkAnnotations = annotateImage.getLandmarkAnnotations();
				if (landmarkAnnotations != null && landmarkAnnotations.size() > 0) {
					landAnnotation = landmarkAnnotations.get(0);
				}
				final List<EntityAnnotation> logoAnnotations = annotateImage.getLogoAnnotations();
				if (logoAnnotations != null && logoAnnotations.size() > 0) {
					logoAnnotation = logoAnnotations.get(0);
				}
				final List<EntityAnnotation> labelAnnotations = annotateImage.getLabelAnnotations();
				if (labelAnnotations != null && labelAnnotations.size() > 0) {
					labelAnnotation = labelAnnotations.get(0);
				}
				final WebDetection webDetection = annotateImage.getWebDetection();
				if (webDetection != null) {
					final List<WebEntity> webEntities = webDetection.getWebEntities();
					if (webEntities != null && webEntities.size() > 0) {
						webEntity = webEntities.get(0);
					}
				}
				mView.addEntities(landAnnotation, logoAnnotation, labelAnnotation, webEntity);
			}
		}
	}

	@Override
	public void setRefreshing(boolean refresh) {
		mView.setRefreshing(refresh);
	}
}
