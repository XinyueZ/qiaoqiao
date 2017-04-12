package com.qiaoqiao.home;


import android.content.res.Resources;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import com.google.android.cameraview.CameraView;
import com.qiaoqiao.databinding.HomeBinding;
import com.qiaoqiao.ds.DsRepository;
import com.qiaoqiao.ds.DsSource;
import com.qiaoqiao.utils.LL;
import com.qiaoqiao.views.MainControlView;

import java.io.File;

import javax.inject.Inject;

import static android.os.Build.VERSION_CODES.JELLY_BEAN;

public final class Home implements HomeContract.Presenter {
	private final @NonNull HomeBinding mBinding;
	private final @NonNull HomeContract.View mView;
	private final @NonNull DsRepository mDsRepository;

	@Inject
	Home(@NonNull HomeContract.View view, @NonNull HomeBinding binding, @NonNull DsRepository dsRepository) {
		mView = view;
		mBinding = binding;
		mDsRepository = dsRepository;
	}

	@Inject
	void onInjected() {
		mBinding.camera.addCallback(mCameraCallback);
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
				capturePhoto();
				mBinding.getUiHelper()
				        .hide();
//				mBinding.loadingPb.setVisibility(View.VISIBLE);
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
		mBinding.camera.removeCallback(mCameraCallback);
		mBinding.camera.stop();
	}

	@Override
	public void changeFocus() {
		mBinding.getUiHelper()
		        .hide();
	}

	@Override
	public void capturePhoto() {
		mBinding.camera.takePicture();
	}

	private final CameraView.Callback mCameraCallback = new CameraView.Callback() {
		@Override
		public void onPictureTaken(final CameraView cameraView, final byte[] data) {
			mDsRepository.compressData(data, new DsSource.BytesLoadedCallback() {
				@Override
				public void onLoaded(@NonNull byte[] data) {
					LL.d("Home-onPictureTaken:" + data.length);
				}
			});
		}

		@Override
		public void onCameraOpened(CameraView cameraView) {
		}

		@Override
		public void onCameraClosed(CameraView cameraView) {
		}
	};

	@Override
	public void copyLink(@NonNull   Uri uri) {
		mDsRepository.readWeb(uri, new DsSource.LocalLoadedCallback() {
			@Override
			public void onLoaded(@NonNull Uri uri) {
				LL.d("Home-copyLink:" + uri);
			}
		});
	}

	@Override
	public void openLocal(@NonNull File file) {
		mDsRepository.readLocal(file, new DsSource.BytesLoadedCallback() {
			@Override
			public void onLoaded(@NonNull byte[] data) {
				LL.d("Home-openLocal:" + data.length);
			}
		});
	}
}
