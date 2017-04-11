package com.qiaoqiao.views;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.qiaoqiao.databinding.LayoutMainControlBinding;

import java.lang.ref.WeakReference;

public final class MainControl extends FrameLayout {
	private LayoutMainControlBinding mBinding;
	private WeakReference<OnFromLocalClickedListener> mOnFromLocalClickedListenerWeakReference;
	private WeakReference<OnCaptureClickedListener> mOnCaptureClickedListenerWeakReference;
	private WeakReference<OnFromWebClickedListener> mOnFromWebClickedListenerWeakReference;

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
		mOnFromLocalClickedListenerWeakReference = new WeakReference<>(l);
		mBinding.fromLocalFb.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mOnFromLocalClickedListenerWeakReference.get() != null) {
					mOnFromLocalClickedListenerWeakReference.get()
					                                        .onClick(v);
				}
			}
		});
	}

	public void setOnCaptureClickedListener(@Nullable OnCaptureClickedListener l) {
		mOnCaptureClickedListenerWeakReference = new WeakReference<>(l);
		mBinding.captureFb.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mOnCaptureClickedListenerWeakReference.get() != null) {
					mOnCaptureClickedListenerWeakReference.get()
					                                      .onClick(v);
				}
			}
		});
	}

	public void setOnFromWebClickedListener(@Nullable OnFromWebClickedListener l) {
		mOnFromWebClickedListenerWeakReference = new WeakReference<>(l);
		mBinding.fromWebFb.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mOnFromWebClickedListenerWeakReference.get() != null) {
					mOnFromWebClickedListenerWeakReference.get()
					                                      .onClick(v);
				}
			}
		});
	}
}
