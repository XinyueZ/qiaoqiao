package com.qiaoqiao.views;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

import com.qiaoqiao.databinding.LayoutMainControlBinding;

public final class MainControl extends FrameLayout {
	private LayoutMainControlBinding mBinding;

	public interface OnFromLocalClickedListener extends OnClickListener {}

	public interface OnCaptureClickedListener extends OnClickListener {}

	public interface OnFromWebClickedListener extends OnClickListener {}


	public MainControl(Context context) {
		super(context);
		init(context);
	}

	public MainControl(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public MainControl(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init(context);
	}

	private void init(@NonNull Context cxt) {
		LayoutInflater inflater = LayoutInflater.from(cxt);
		mBinding = LayoutMainControlBinding.inflate(inflater, this, true);
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
