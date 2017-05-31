package com.qiaoqiao.core.confidence;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;

import com.qiaoqiao.app.PrefsKeys;
import com.qiaoqiao.core.confidence.ui.Confidence;

import javax.inject.Inject;

public final class ConfidencePresenter implements ConfidenceContract.Presenter {
	private @NonNull ConfidenceContract.View mView;

	@Inject
	ConfidencePresenter(@NonNull ConfidenceContract.View v) {
		mView = v;
	}

	@Inject
	void injected() {
		mView.setPresenter(this);
	}

	@Override
	public void begin(@NonNull FragmentActivity hostActivity) {

	}

	@Override
	public void end(@NonNull FragmentActivity hostActivity) {

	}

	@Override
	public void save(@NonNull Context context, @NonNull String key, int progress) {
		new Confidence(key, progress / 100.0f).save(context);
	}

	@Override
	public void loadAllConfidences(@NonNull Context cxt) {
		mView.showLabelConfidence(Confidence.createFromPrefs(cxt, PrefsKeys.KEY_CONFIDENCE_LABEL));
		mView.showLogoConfidence(Confidence.createFromPrefs(cxt, PrefsKeys.KEY_CONFIDENCE_LOGO));
		mView.showImageConfidence(Confidence.createFromPrefs(cxt, PrefsKeys.KEY_CONFIDENCE_IMAGE));
	}
}
