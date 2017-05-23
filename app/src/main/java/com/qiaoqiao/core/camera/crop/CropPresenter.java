package com.qiaoqiao.core.camera.crop;


import android.support.annotation.NonNull;

import javax.inject.Inject;

public final class CropPresenter implements CropContract.Presenter {
	private final @NonNull CropContract.View mView;


	@Inject
	CropPresenter(@NonNull CropContract.View view) {
		mView = view;
	}

	@Inject
	void onInjected() {
		mView.setPresenter(this);
	}

	@Override
	public void begin() {
	}

	@Override
	public void end() {
	}

	@Override
	public void setImageData(byte[] data) {
		mView.setImageData(data);
	}
}
