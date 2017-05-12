package com.qiaoqiao.vision;


import android.support.annotation.NonNull;

import com.google.api.services.vision.v1.model.AnnotateImageResponse;
import com.google.api.services.vision.v1.model.BatchAnnotateImagesResponse;
import com.google.api.services.vision.v1.model.EntityAnnotation;
import com.google.api.services.vision.v1.model.WebDetection;
import com.google.api.services.vision.v1.model.WebEntity;
import com.qiaoqiao.vision.bus.VisionEntityClickEvent;

import java.util.List;

import javax.inject.Inject;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;

public final class VisionPresenter implements VisionContract.Presenter {
	private final @NonNull VisionContract.View mView;

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
		mView.showDetail(e.getEntity());

	}

	//------------------------------------------------


	@Inject
	VisionPresenter(@NonNull VisionContract.View view) {
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

		mView.showList();
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
						mView.addLandmarkEntity(entityAnnotation);
					}
				}
				final WebDetection webDetection = annotateImage.getWebDetection();
				if (webDetection != null) {
					final List<WebEntity> webEntities = webDetection.getWebEntities();
					if (webEntities != null && webEntities.size() > 0) {
						final WebEntity webEntity = webEntities.get(0);
						if (webEntity != null) {
							mView.addWebEntity(webEntity);
						}
					}
				}
			}
		}
	}
}
