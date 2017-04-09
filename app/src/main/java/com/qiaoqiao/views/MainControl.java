package com.qiaoqiao.views;


import android.content.Context;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

import com.qiaoqiao.databinding.LayoutMainControlBinding;

public final class MainControl extends FrameLayout {
	private LayoutMainControlBinding mBinding;

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
}
