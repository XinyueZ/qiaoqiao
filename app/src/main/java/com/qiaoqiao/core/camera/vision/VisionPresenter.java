package com.qiaoqiao.core.camera.vision;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.google.api.services.vision.v1.model.AnnotateImageResponse;
import com.google.api.services.vision.v1.model.BatchAnnotateImagesResponse;
import com.google.api.services.vision.v1.model.EntityAnnotation;
import com.google.api.services.vision.v1.model.WebDetection;
import com.google.api.services.vision.v1.model.WebEntity;
import com.qiaoqiao.core.camera.vision.bus.VisionEntityClickEvent;
import com.qiaoqiao.core.camera.vision.model.VisionEntity;
import com.qiaoqiao.core.confidence.ui.Confidence;
import com.qiaoqiao.repository.DsRepository;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import de.greenrobot.event.Subscribe;

import static com.qiaoqiao.app.PrefsKeys.DEFAULT_CONFIDENCE_IMAGE;
import static com.qiaoqiao.app.PrefsKeys.DEFAULT_CONFIDENCE_LABEL;
import static com.qiaoqiao.app.PrefsKeys.DEFAULT_CONFIDENCE_LOGO;
import static com.qiaoqiao.app.PrefsKeys.KEY_CONFIDENCE_IMAGE;
import static com.qiaoqiao.app.PrefsKeys.KEY_CONFIDENCE_LABEL;
import static com.qiaoqiao.app.PrefsKeys.KEY_CONFIDENCE_LOGO;

public final class VisionPresenter extends VisionContract.Presenter {
	private final @NonNull VisionContract.View mView;
	private final Reference<Context> mContextReference;

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
	VisionPresenter(@Nullable Context cxt, @NonNull VisionContract.View view, @NonNull DsRepository dsRepository) {
		super(dsRepository);
		mContextReference = new WeakReference<>(cxt);
		mView = view;
	}

	@Inject
	void onInjected() {
		mView.setPresenter(this);
	}

	@Override
	public void addResponseToScreen(@NonNull BatchAnnotateImagesResponse response) {
		if (mContextReference.get() == null) {
			return;
		}
		final List<AnnotateImageResponse> annotates = response.getResponses();
		List<VisionEntity> filterList = new ArrayList<>();
		if (annotates != null && annotates.size() > 0) {
			final AnnotateImageResponse annotateImage = annotates.get(0);
			if (annotateImage != null) {
				final List<EntityAnnotation> labelAnnotations = annotateImage.getLabelAnnotations();
				if (labelAnnotations != null && labelAnnotations.size() > 0) {
					for (EntityAnnotation annotation : labelAnnotations) {
						if (!TextUtils.isEmpty(annotation.getDescription()) && annotation.getScore() > Confidence.getValueOnly(mContextReference.get(),
						                                                                                                       KEY_CONFIDENCE_LABEL,
						                                                                                                       DEFAULT_CONFIDENCE_LABEL)) {
							filterList.add(new VisionEntity(annotation, "LABEL_DETECTION").setActivated(true));
						}
					}
				}

				final List<EntityAnnotation> landmarkAnnotations = annotateImage.getLandmarkAnnotations();
				if (landmarkAnnotations != null && landmarkAnnotations.size() > 0) {
					for (EntityAnnotation annotation : landmarkAnnotations) {
						if (!TextUtils.isEmpty(annotation.getDescription()) && annotation.getScore() > Confidence.getValueOnly(mContextReference.get(),
						                                                                                                       KEY_CONFIDENCE_IMAGE,
						                                                                                                       DEFAULT_CONFIDENCE_IMAGE)) {
							filterList.add(new VisionEntity(annotation, "LANDMARK_DETECTION").setActivated(true));
						}
					}
				}
				final List<EntityAnnotation> logoAnnotations = annotateImage.getLogoAnnotations();
				if (logoAnnotations != null && logoAnnotations.size() > 0) {
					for (EntityAnnotation annotation : logoAnnotations) {
						if (!TextUtils.isEmpty(annotation.getDescription()) && annotation.getScore() > Confidence.getValueOnly(mContextReference.get(), KEY_CONFIDENCE_LOGO,
						                                                                                                       DEFAULT_CONFIDENCE_LOGO)) {
							filterList.add(new VisionEntity(annotation, "LOGO_DETECTION").setActivated(true));
						}
					}
				}

				final WebDetection webDetection = annotateImage.getWebDetection();
				if (webDetection != null) {
					final List<WebEntity> webEntities = webDetection.getWebEntities();
					if (webEntities != null && webEntities.size() > 0) {
						for (WebEntity entity : webEntities) {
							if (!TextUtils.isEmpty(entity.getDescription()) && entity.getScore() > Confidence.createFromPrefs(mContextReference.get(), KEY_CONFIDENCE_IMAGE, DEFAULT_CONFIDENCE_IMAGE)
							                                                                                 .getValue()) {
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

	@Override
	public void clear() {
		mView.clear();
	}
}
