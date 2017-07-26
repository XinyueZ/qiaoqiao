package com.qiaoqiao.core.camera;


import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;

import com.google.api.services.vision.v1.model.BatchAnnotateImagesResponse;
import com.google.api.services.vision.v1.model.Status;
import com.qiaoqiao.core.camera.vision.VisionPresenter;
import com.qiaoqiao.repository.DsLoadedCallback;
import com.qiaoqiao.repository.DsRepository;

import javax.inject.Inject;

public final class CameraPresenter implements CameraContract.Presenter {
	private final @NonNull CameraContract.View mView;
	private final @NonNull DsRepository mDsRepository;
	private VisionPresenter mVisionPresenter;

	@Inject
	CameraPresenter(@NonNull CameraContract.View view, @NonNull DsRepository dsRepository) {
		mView = view;
		mDsRepository = dsRepository;
	}

	@Inject
	void onInjected() {
		mView.setPresenter(this);
	}

	public void setVisionPresenter(VisionPresenter visionPresenter) {
		mVisionPresenter = visionPresenter;
		visionPresenter.setCameraPresenter(this);
	}

	@Override
	public void begin(@NonNull FragmentActivity hostActivity) {
	}

	@Override
	public void end(@NonNull FragmentActivity hostActivity) {
	}


	@Override
	public void updateWhenResponse() {
		mView.updateViewWhenResponse();
	}

	@Override
	public void findAnnotateImages(@NonNull byte[] bytes) {
		mView.updateViewWhenRequest();
		mDsRepository.onBytes(bytes, new DsLoadedCallback() {
			@Override
			public void onVisionResponse(BatchAnnotateImagesResponse response) {
				super.onVisionResponse(response);
				mVisionPresenter.addResponseToScreen(response, true);
			}

			@Override
			public void onVisionApiError(@NonNull Status status) {
				super.onVisionApiError(status);
				updateWhenResponse();
			}

			@Override
			public void onException(@NonNull Exception e) {
				super.onException(e);
				mView.showError(e.toString());
				updateWhenResponse();
			}
		});
	}
}
