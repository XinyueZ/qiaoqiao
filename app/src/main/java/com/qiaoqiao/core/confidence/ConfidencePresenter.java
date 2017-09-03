package com.qiaoqiao.core.confidence;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;

import com.qiaoqiao.core.confidence.ui.Confidence;

import javax.inject.Inject;

import static com.qiaoqiao.app.ConstsKt.DEFAULT_CONFIDENCE_IMAGE;
import static com.qiaoqiao.app.ConstsKt.DEFAULT_CONFIDENCE_LABEL;
import static com.qiaoqiao.app.ConstsKt.DEFAULT_CONFIDENCE_LOGO;
import static com.qiaoqiao.app.ConstsKt.KEY_CONFIDENCE_IMAGE;
import static com.qiaoqiao.app.ConstsKt.KEY_CONFIDENCE_LABEL;
import static com.qiaoqiao.app.ConstsKt.KEY_CONFIDENCE_LOGO;

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
	public @NonNull
	ConfidenceContract.Presenter save(@NonNull Context context, @NonNull Confidence onSaveItem) {
		onSaveItem.save(context);
		return this;
	}

	@Override
	public void loadAllConfidences(@NonNull Context cxt) {
		mView.showLabelConfidence(Confidence.createFromPrefs(cxt, KEY_CONFIDENCE_LABEL, DEFAULT_CONFIDENCE_LABEL));
		mView.showLogoConfidence(Confidence.createFromPrefs(cxt, KEY_CONFIDENCE_LOGO, DEFAULT_CONFIDENCE_LOGO));
		mView.showImageConfidence(Confidence.createFromPrefs(cxt, KEY_CONFIDENCE_IMAGE, DEFAULT_CONFIDENCE_IMAGE));
	}
}
