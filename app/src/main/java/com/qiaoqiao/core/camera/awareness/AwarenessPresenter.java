package com.qiaoqiao.core.camera.awareness;


import android.support.annotation.NonNull;

import javax.inject.Inject;

public final class AwarenessPresenter implements AwarenessContract.Presenter {
	private final @NonNull AwarenessContract.View mView;

	@Inject
	AwarenessPresenter(@NonNull AwarenessContract.View view) {
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
		//Still not impl.
	}

}
