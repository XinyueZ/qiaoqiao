package com.qiaoqiao.core.camera.crop;


import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;

import javax.inject.Inject;

public final class CropPresenter implements CropContract.Presenter {
	private final @NonNull CropContract.View mView;
	private final @NonNull CropCallback mCropCallback;

	@Inject
	CropPresenter(@NonNull CropContract.View view, @NonNull CropCallback cropCallback) {
		mView = view;
		mCropCallback = cropCallback;
	}

	@Inject
	void onInjected() {
		mView.setPresenter(this);
	}

	@Override
	public void begin(@NonNull FragmentActivity hostActivity) {
	}

	@Override
	public void end(@NonNull FragmentActivity hostActivity) {
	}

	@Override
	public void setImageData(@NonNull byte[] data) {
		mView.setImageData(data);
	}

	@Override
	public void cropped(@NonNull byte[] bytes) {
		mCropCallback.onCropped(bytes);
	}

	@Override
	public void croppedFail() {
		mCropCallback.onCroppedFail();
	}
}
