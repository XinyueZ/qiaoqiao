package com.qiaoqiao.home;


import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import com.google.android.cameraview.CameraView;
import com.qiaoqiao.R;
import com.qiaoqiao.backend.model.response.AnnotateImageResponseCollection;
import com.qiaoqiao.databinding.HomeBinding;
import com.qiaoqiao.ds.AbstractDsSource;
import com.qiaoqiao.ds.DsRepository;
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
			mDsRepository.captureCamera(data, new AbstractDsSource.BytesLoadedCallback() {
				@Override
				public void onLoaded(@NonNull byte[] data) {
					LL.d("Home-onPictureTaken:" + data.length);
				}

				@Override
				public void onVisionResponse(@NonNull AnnotateImageResponseCollection response) {
					LL.d("Home-onVisionResponse");
				}

				@Override
				public void onError(@NonNull Exception e) {
					mView.showError(mBinding.home, e.toString());
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
	public void openLink(@NonNull Uri uri) {
		mDsRepository.openWebLink(uri, new AbstractDsSource.OpenWebLinkCallback() {
			@Override
			public void onOpened(@NonNull Uri uri) {
				LL.d("Home-openLink:" + uri);
			}

			@Override
			public void onVisionResponse(@NonNull AnnotateImageResponseCollection response) {
				LL.d("Home-onVisionResponse");
			}
		});
	}

	@Override
	public void openLocal(@NonNull Context cxt, @NonNull Uri uri) {
		Cursor cursor = null;
		try {
			String[] filePathColumn = { MediaStore.Images.Media.DATA };
			cursor = cxt.getContentResolver()
			            .query(uri, filePathColumn, null, null, null);
			if (cursor == null || cursor.getCount() < 1) {
				mView.showError(mBinding.home, cxt.getString(R.string.error_can_not_find_file));
				return;
			}
			cursor.moveToFirst();
			int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
			if (columnIndex < 0) {
				mView.showError(mBinding.home, cxt.getString(R.string.error_can_not_find_file));
				return;
			}
			mDsRepository.readLocal(new File(cursor.getString(columnIndex)), new AbstractDsSource.BytesLoadedCallback() {
				@Override
				public void onLoaded(@NonNull byte[] data) {
					LL.d("Home-openLocal:" + data.length);
				}

				@Override
				public void onVisionResponse(@NonNull AnnotateImageResponseCollection response) {
					LL.d("Home-onVisionResponse");
				}

				@Override
				public void onError(@NonNull Exception e) {
					mView.showError(mBinding.home, e.toString());
				}
			});
		} catch (Exception e) {
			mView.showError(mBinding.home, cxt.getString(R.string.error_can_not_find_file));
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
	}
}
