package com.qiaoqiao.home;


import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import com.google.android.cameraview.CameraView;
import com.google.api.services.vision.v1.model.BatchAnnotateImagesResponse;
import com.google.api.services.vision.v1.model.Status;
import com.qiaoqiao.R;
import com.qiaoqiao.ds.DsLoadedCallback;
import com.qiaoqiao.ds.DsRepository;
import com.qiaoqiao.utils.LL;

import java.io.File;

import javax.inject.Inject;

import static android.os.Build.VERSION_CODES.JELLY_BEAN;

public final class Home implements HomeContract.Presenter {
	private final @NonNull HomeContract.View mHomeView;
	private final @NonNull DsRepository mDsRepository;

	@Inject
	Home(@NonNull HomeContract.View homeView, @NonNull DsRepository dsRepository) {
		mHomeView = homeView;
		mDsRepository = dsRepository;
	}

	@Inject
	void onInjected() {
		mHomeView.getBinding().mainControl.setOnFromLocalClickedListener(v -> {
			mHomeView.showLoadFromLocal();
			mHomeView.getBinding()
			         .getUiHelper()
			         .hide();
		});
		mHomeView.getBinding().mainControl.setOnCaptureClickedListener(v -> {
			capturePhoto();
			mHomeView.getBinding()
			         .getUiHelper()
			         .hide();
		});
		mHomeView.getBinding().mainControl.setOnFromWebClickedListener(v -> {
			mHomeView.showInputFromWeb();
			mHomeView.getBinding()
			         .getUiHelper()
			         .hide();
		});

		mHomeView.setPresenter(this);
	}

	@Override
	public void begin() {
		mHomeView.getBinding().camera.addCallback(mCameraCallback);
		mHomeView.getBinding().camera.start();
		mHomeView.getBinding()
		         .getDecorView()
		         .getViewTreeObserver()
		         .addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
			         @Override
			         public void onGlobalLayout() {
				         final ViewGroup decorView = mHomeView.getBinding()
				                                              .getDecorView();
				         if (Build.VERSION.SDK_INT >= JELLY_BEAN) {
					         decorView.getViewTreeObserver()
					                  .removeOnGlobalLayoutListener(this);
				         } else {
					         decorView.getViewTreeObserver()
					                  .removeGlobalOnLayoutListener(this);
				         }
				         Rect rect = new Rect();
				         decorView.getWindowVisibleDisplayFrame(rect);
				         mHomeView.getBinding().home.setPadding(0, rect.top, 0, 0);
			         }
		         });
		final Resources resources = mHomeView.getBinding()
		                                     .getDecorView()
		                                     .getContext()
		                                     .getResources();
		int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
		if (resourceId > 0) {
			((ViewGroup.MarginLayoutParams) mHomeView.getBinding().mainControl.getLayoutParams()).bottomMargin = resources.getDimensionPixelSize(resourceId);
		}
	}

	@Override
	public void stop() {
		mHomeView.getBinding().camera.removeCallback(mCameraCallback);
		mHomeView.getBinding().camera.stop();
	}


	@Override
	public void capturePhoto() {
		mHomeView.getBinding().mainControl.startCaptureProgressBar();
		mHomeView.getBinding().camera.takePicture();
	}

	private final CameraView.Callback mCameraCallback = new CameraView.Callback() {
		@Override
		public void onPictureTaken(final CameraView cameraView, final byte[] data) {
			if (data == null) {
				LL.e("The camera captured picture but the bytes is NULL.");
				return;
			}
			mDsRepository.onBytes(data, new DsLoadedCallback() {
				@Override
				public void onVisionResponse(BatchAnnotateImagesResponse response) {
					super.onVisionResponse(response);
					mHomeView.getBinding().mainControl.stopCaptureProgressBar();
					mHomeView.addResponseToScreen(response);
				}

				@Override
				public void onError(@NonNull Status status) {
					super.onError(status);
					mHomeView.getBinding().mainControl.stopCaptureProgressBar();
				}

				@Override
				public void onException(@NonNull Exception e) {
					super.onException(e);
					mHomeView.getBinding().mainControl.stopCaptureProgressBar();
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
		mHomeView.getBinding().mainControl.startWebProgressBar();
		mDsRepository.onUri(uri, new DsLoadedCallback() {
			@Override
			public void onVisionResponse(BatchAnnotateImagesResponse response) {
				super.onVisionResponse(response);
				mHomeView.getBinding().mainControl.stopWebProgressBar();
				mHomeView.addResponseToScreen(response);
			}

			@Override
			public void onError(@NonNull Status status) {
				super.onError(status);
				mHomeView.getBinding().mainControl.stopWebProgressBar();
			}

			@Override
			public void onException(@NonNull Exception e) {
				super.onException(e);
				mHomeView.showError(mHomeView.getBinding().home, e.toString());
				mHomeView.getBinding().mainControl.stopWebProgressBar();
			}
		});
	}

	@Override
	public void openLocal(@NonNull Context cxt, @NonNull Uri uri) {
		Cursor cursor = null;
		try {
			mHomeView.getBinding().mainControl.startLocalProgressBar();
			String[] filePathColumn = { MediaStore.Images.Media.DATA };
			cursor = cxt.getContentResolver()
			            .query(uri, filePathColumn, null, null, null);
			if (cursor == null || cursor.getCount() < 1) {
				mHomeView.showError(mHomeView.getBinding().home, cxt.getString(R.string.error_can_not_find_file));
				return;
			}
			cursor.moveToFirst();
			int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
			if (columnIndex < 0) {
				mHomeView.showError(mHomeView.getBinding().home, cxt.getString(R.string.error_can_not_find_file));
				return;
			}
			mDsRepository.onFile(new File(cursor.getString(columnIndex)), new DsLoadedCallback() {
				@Override
				public void onVisionResponse(BatchAnnotateImagesResponse response) {
					super.onVisionResponse(response);
					mHomeView.getBinding().mainControl.stopLocalProgressBar();
					mHomeView.addResponseToScreen(response);
				}

				@Override
				public void onError(@NonNull Status status) {
					super.onError(status);
					mHomeView.getBinding().mainControl.stopLocalProgressBar();
				}

				@Override
				public void onException(@NonNull Exception e) {
					super.onException(e);
					mHomeView.showError(mHomeView.getBinding().home, e.toString());
					mHomeView.getBinding().mainControl.stopLocalProgressBar();
				}
			});
		} catch (Exception e) {
			mHomeView.showError(mHomeView.getBinding().home, cxt.getString(R.string.error_can_not_find_file));
			mHomeView.getBinding().mainControl.stopLocalProgressBar();
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
	}
}
