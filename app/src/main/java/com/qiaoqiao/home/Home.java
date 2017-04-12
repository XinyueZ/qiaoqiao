package com.qiaoqiao.home;


import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Build;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Toast;

import com.qiaoqiao.databinding.HomeBinding;
import com.qiaoqiao.views.MainControlView;

import javax.inject.Inject;

import static android.os.Build.VERSION_CODES.JELLY_BEAN;

public final class Home implements HomeContract.Presenter {
	private final @NonNull HomeBinding mBinding;
	private final @NonNull HomeContract.View mView;

	@Inject
	Home(@NonNull HomeContract.View view, @NonNull HomeBinding binding) {
		mView = view;
		mBinding = binding;
		mBinding.mainControl.setOnFromLocalClickedListener(new MainControlView.OnFromLocalClickedListener() {
			@Override
			public void onClick(View v) {
				mView.showLoadFromLocal();
				mBinding.getUiHelper()
				        .hide();
//				mBinding.loadingPb.setVisibility(View.VISIBLE);
			}
		});
		mBinding.mainControl.setOnCaptureClickedListener(new MainControlView.OnCaptureClickedListener() {
			@Override
			public void onClick(View v) {
				Toast.makeText(v.getContext(), "setOnCaptureClickedListener", Toast.LENGTH_SHORT)
				     .show();

				mBinding.getUiHelper()
				        .hide();
				mBinding.loadingPb.setVisibility(View.VISIBLE);
			}
		});
		mBinding.mainControl.setOnFromWebClickedListener(new MainControlView.OnFromWebClickedListener() {
			@Override
			public void onClick(View v) {
				mView.showInputFromWeb();
				mBinding.getUiHelper()
				        .hide();
//				mBinding.loadingPb.setVisibility(View.VISIBLE);
			}
		});
	}

	@Inject
	void onInject() {
		mView.setPresenter(this);
	}

	@Override
	public void start() {
		mBinding.camera.start();
		mBinding.getDecorView()
		        .getViewTreeObserver()
		        .addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
			        @Override
			        public void onGlobalLayout() {
				        final ViewGroup decorView = mBinding.getDecorView();
				        if (Build.VERSION.SDK_INT >= JELLY_BEAN) {
					        decorView.getViewTreeObserver()
					                 .removeOnGlobalLayoutListener(this);
				        } else {
					        decorView.getViewTreeObserver()
					                 .removeGlobalOnLayoutListener(this);
				        }
				        Rect rect = new Rect();
				        decorView.getWindowVisibleDisplayFrame(rect);
				        mBinding.home.setPadding(0, rect.top, 0, 0);
			        }
		        });
		final Resources resources = mBinding.getDecorView()
		                                    .getContext()
		                                    .getResources();
		int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
		if (resourceId > 0) {
			((ViewGroup.MarginLayoutParams) mBinding.mainControl.getLayoutParams()).bottomMargin = resources.getDimensionPixelSize(resourceId);
		}
	}

	@Override
	public void stop() {
		mBinding.camera.stop();
	}

	@Override
	public void changeFocus() {
		mBinding.getUiHelper()
		        .hide();
	}
}
