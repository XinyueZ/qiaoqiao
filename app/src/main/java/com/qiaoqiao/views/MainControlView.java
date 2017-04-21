package com.qiaoqiao.views;


import android.content.Context;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

import com.qiaoqiao.databinding.LayoutMainControlBinding;

public final class MainControlView extends FrameLayout {
	private LayoutMainControlBinding mBinding;

	public interface OnFromLocalClickedListener extends OnClickListener {}

	public interface OnCaptureClickedListener extends OnClickListener {}

	public interface OnFromWebClickedListener extends OnClickListener {}


	public MainControlView(Context context) {
		super(context);
		init(context);
	}

	public MainControlView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public MainControlView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init(context);
	}

	private void init(@NonNull Context cxt) {
		LayoutInflater inflater = LayoutInflater.from(cxt);
		mBinding = LayoutMainControlBinding.inflate(inflater, this, true);
	}

	public void startCaptureProgressBar() {
		animateProgressBar(mBinding.captureFbPb.getDrawable());
		mBinding.captureFb.setEnabled(false);
	}

	public void stopCaptureProgressBar() {
		stopAnimateProgressBar(mBinding.captureFbPb.getDrawable());
		mBinding.captureFb.setEnabled(true);
	}

	public void startLocalProgressBar() {
		animateProgressBar(mBinding.fromLocalFbPb.getDrawable());
		mBinding.fromLocalFb.setEnabled(false);
	}

	public void stopLocalProgressBar() {
		stopAnimateProgressBar(mBinding.fromLocalFbPb.getDrawable());
		mBinding.fromLocalFb.setEnabled(true);
	}

	public void startWebProgressBar() {
		animateProgressBar(mBinding.fromWebFbPb.getDrawable());
		mBinding.fromWebFb.setEnabled(false);
	}

	public void stopWebProgressBar() {
		stopAnimateProgressBar(mBinding.fromWebFbPb.getDrawable());
		mBinding.fromWebFb.setEnabled(true);
	}

	private void animateProgressBar(Drawable drawable) {
		if (drawable instanceof Animatable) {
			((Animatable) drawable).start();
		}
	}

	private void stopAnimateProgressBar(Drawable drawable) {
		if (drawable instanceof Animatable) {
			((Animatable) drawable).stop();
		}
	}

	public void setOnFromLocalClickedListener(@Nullable OnFromLocalClickedListener l) {
		mBinding.fromLocalFb.setOnClickListener(l);
	}

	public void setOnCaptureClickedListener(@Nullable OnCaptureClickedListener l) {
		mBinding.captureFb.setOnClickListener(l);
	}

	public void setOnFromWebClickedListener(@Nullable OnFromWebClickedListener l) {
		mBinding.fromWebFb.setOnClickListener(l);
	}
}
