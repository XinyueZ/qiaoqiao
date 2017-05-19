package com.qiaoqiao.views;


import android.content.Context;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.qiaoqiao.databinding.LayoutLocatingControlBinding;

public final class LocatingControlView extends FrameLayout {
	private LayoutLocatingControlBinding mBinding;
	private Vibrator mVibrator;
	private static final int VIB_LNG = 50;


	public interface OnFromLocalClickedListener extends OnClickListener {}


	public LocatingControlView(Context context) {
		super(context);
		init(context);
	}

	public LocatingControlView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public LocatingControlView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init(context);
	}

	private void init(@NonNull Context cxt) {
		LayoutInflater inflater = LayoutInflater.from(cxt);
		mBinding = LayoutLocatingControlBinding.inflate(inflater, this, true);

		mVibrator = (Vibrator) cxt.getSystemService(Context.VIBRATOR_SERVICE);
	}


	public void startLocalProgressBar() {
		animateProgressBar(mBinding.fromLocalFbPb);
		mBinding.fromLocalFb.setEnabled(false);
	}

	public void stopLocalProgressBar() {
		stopAnimateProgressBar(mBinding.fromLocalFbPb);
		mBinding.fromLocalFb.setEnabled(true);
	}


	private void animateProgressBar(ImageView view) {
		final Drawable drawable = view.getDrawable();
		if (drawable instanceof Animatable) {
			((Animatable) drawable).start();
		}
		view.setVisibility(VISIBLE);
	}

	private void stopAnimateProgressBar(ImageView view) {
		final Drawable drawable = view.getDrawable();
		if (drawable instanceof Animatable) {
			((Animatable) drawable).stop();
		}
		view.setVisibility(INVISIBLE);
	}

	public void setOnFromLocalClickedListener(@Nullable OnFromLocalClickedListener l) {
		mBinding.fromLocalFb.setOnClickListener(v -> {
			mVibrator.vibrate(VIB_LNG);
			if (l != null) {
				l.onClick(v);
			}
		});
	}

}
