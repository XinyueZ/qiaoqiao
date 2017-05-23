package com.qiaoqiao.core.camera.vision;


import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.api.services.vision.v1.model.AnnotateImageResponse;
import com.google.api.services.vision.v1.model.BatchAnnotateImagesResponse;
import com.google.api.services.vision.v1.model.EntityAnnotation;
import com.google.api.services.vision.v1.model.WebDetection;
import com.google.api.services.vision.v1.model.WebEntity;
import com.qiaoqiao.core.camera.vision.annotation.target.More;
import com.qiaoqiao.core.camera.vision.bus.VisionEntityClickEvent;
import com.qiaoqiao.core.camera.vision.model.VisionEntity;
import com.qiaoqiao.repository.DsRepository;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import de.greenrobot.event.Subscribe;

public final class MoreVisionPresenter extends VisionContract.Presenter {
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
	MoreVisionPresenter(@NonNull @More VisionContract.View view, @NonNull DsRepository dsRepository) {
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

		List<VisionEntity> output = new ArrayList<>();
		if (annotates != null && annotates.size() > 0) {
			final AnnotateImageResponse annotateImage = annotates.get(0);
			if (annotateImage != null) {
				final WebDetection webDetection = annotateImage.getWebDetection();
				if (webDetection != null) {
					addWebEntity(webDetection.getWebEntities(), output);
				}
				addEntities(annotateImage.getLandmarkAnnotations(), "LANDMARK_DETECTION", output);
				addEntities(annotateImage.getLogoAnnotations(), "LOGO_DETECTION", output);
				addEntities(annotateImage.getLabelAnnotations(), "LABEL_DETECTION", output);
			}
		}
		mView.addEntities(output);
	}

	public void clean() {
		mView.clean();
	}

	@Override
	public void setRefreshing(boolean refresh) {
		mView.setRefreshing(refresh);
	}


	private void addEntities(@Nullable List<EntityAnnotation> entityAnnotationList, @NonNull String name, @NonNull List<VisionEntity> output) {
		if (entityAnnotationList == null || entityAnnotationList.size() <= 0) {
			return;
		}
		for (EntityAnnotation entityAnnotation : entityAnnotationList) {
			output.add(new VisionEntity(entityAnnotation, name, true));
		}

	}


	private void addWebEntity(@Nullable List<WebEntity> webEntityList, @NonNull List<VisionEntity> output) {
		if (webEntityList == null || webEntityList.size() <= 0) {
			return;
		}
		for (WebEntity entityAnnotation : webEntityList) {
			output.add(new VisionEntity(entityAnnotation, "WEB_DETECTION", true));
		}
	}
}
