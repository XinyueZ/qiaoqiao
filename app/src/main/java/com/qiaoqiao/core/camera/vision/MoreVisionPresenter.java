package com.qiaoqiao.core.camera.vision;


import android.support.annotation.NonNull;

import com.google.api.services.vision.v1.model.AnnotateImageResponse;
import com.google.api.services.vision.v1.model.BatchAnnotateImagesResponse;
import com.google.api.services.vision.v1.model.EntityAnnotation;
import com.google.api.services.vision.v1.model.WebDetection;
import com.google.api.services.vision.v1.model.WebEntity;
import com.qiaoqiao.repository.DsRepository;
import com.qiaoqiao.core.camera.vision.annotation.target.More;
import com.qiaoqiao.core.camera.vision.bus.VisionEntityClickEvent;

import java.util.List;

import javax.inject.Inject;

import de.greenrobot.event.Subscribe;

public final class MoreVisionPresenter extends VisionContract.Presenter {
	private final @NonNull VisionContract.View<List<EntityAnnotation>, List<EntityAnnotation>, List<EntityAnnotation>, List<WebEntity>> mView;

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
		if (annotates != null && annotates.size() > 0) {
			List<WebEntity> webEntities = null;
			final AnnotateImageResponse annotateImage = annotates.get(0);
			if (annotateImage != null) {
				final WebDetection webDetection = annotateImage.getWebDetection();
				if (webDetection != null) {
					webEntities = webDetection.getWebEntities();
				}
				mView.addEntities(annotateImage.getLandmarkAnnotations(), annotateImage.getLogoAnnotations(), annotateImage.getLabelAnnotations(), webEntities);
			}
		}
	}

	public void clean() {
		mView.clean();
	}

	@Override
	public void setRefreshing(boolean refresh) {
		mView.setRefreshing(refresh);
	}
}