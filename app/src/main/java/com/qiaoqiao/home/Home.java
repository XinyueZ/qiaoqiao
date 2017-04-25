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
import com.qiaoqiao.backend.model.wikipedia.WikiResult;
import com.qiaoqiao.databinding.HomeBinding;
import com.qiaoqiao.ds.AbstractDsSource;
import com.qiaoqiao.ds.DsRepository;
import com.qiaoqiao.utils.LL;

import java.io.File;

import javax.inject.Inject;

import static android.os.Build.VERSION_CODES.JELLY_BEAN;

public final class Home implements HomeContract.Presenter {
	private final @NonNull HomeBinding mHomeBinding;
	private final @NonNull HomeContract.View mHomeView;
	private final @NonNull DsRepository mDsRepository;

	@Inject
	Home(@NonNull HomeContract.View homeView, @NonNull HomeBinding homeBinding, @NonNull DsRepository dsRepository) {
		mHomeView = homeView;
		mHomeBinding = homeBinding;
		mDsRepository = dsRepository;
	}

	@Inject
	void onInjected() {
		mHomeBinding.mainControl.setOnFromLocalClickedListener(v -> {
			mHomeView.showLoadFromLocal();
			mHomeBinding.getUiHelper()
			            .hide();
//				mHomeBinding.loadingPb.setVisibility(View.VISIBLE);
		});
		mHomeBinding.mainControl.setOnCaptureClickedListener(v -> {
			capturePhoto();
			mHomeBinding.getUiHelper()
			            .hide();
//				mHomeBinding.loadingPb.setVisibility(View.VISIBLE);
		});
		mHomeBinding.mainControl.setOnFromWebClickedListener(v -> {
			mHomeView.showInputFromWeb();
			mHomeBinding.getUiHelper()
			            .hide();
//				mHomeBinding.loadingPb.setVisibility(View.VISIBLE);
		});

		mHomeView.setPresenter(this);
	}

	@Override
	public void start() {
		mHomeBinding.camera.addCallback(mCameraCallback);
		mHomeBinding.camera.start();
		mHomeBinding.getDecorView()
		            .getViewTreeObserver()
		            .addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
			            @Override
			            public void onGlobalLayout() {
				            final ViewGroup decorView = mHomeBinding.getDecorView();
				            if (Build.VERSION.SDK_INT >= JELLY_BEAN) {
					            decorView.getViewTreeObserver()
					                     .removeOnGlobalLayoutListener(this);
				            } else {
					            decorView.getViewTreeObserver()
					                     .removeGlobalOnLayoutListener(this);
				            }
				            Rect rect = new Rect();
				            decorView.getWindowVisibleDisplayFrame(rect);
				            mHomeBinding.home.setPadding(0, rect.top, 0, 0);
			            }
		            });
		final Resources resources = mHomeBinding.getDecorView()
		                                        .getContext()
		                                        .getResources();
		int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
		if (resourceId > 0) {
			((ViewGroup.MarginLayoutParams) mHomeBinding.mainControl.getLayoutParams()).bottomMargin = resources.getDimensionPixelSize(resourceId);
		}
	}

	@Override
	public void stop() {
		mHomeBinding.camera.removeCallback(mCameraCallback);
		mHomeBinding.camera.stop();
	}

	@Override
	public void changeFocus() {
		mHomeBinding.getUiHelper()
		            .hide();
	}

	@Override
	public void capturePhoto() {
		mHomeBinding.mainControl.startCaptureProgressBar();
		mHomeBinding.camera.takePicture();
	}

	private final CameraView.Callback mCameraCallback = new CameraView.Callback() {
		@Override
		public void onPictureTaken(final CameraView cameraView, final byte[] data) {
			if (data == null) {
				LL.e("The camera captured picture but the bytes is NULL.");
				return;
			}
			mDsRepository.onBytes(data, new AbstractDsSource.LoadedCallback() {
				@Override
				public void onVisionResponse(BatchAnnotateImagesResponse response) {
					super.onVisionResponse(response);
					mHomeBinding.mainControl.stopCaptureProgressBar();
					mHomeView.addResponseToScreen(response);
				}

				@Override
				public void onError(@NonNull Status status) {
					super.onError(status);
					mHomeBinding.mainControl.stopCaptureProgressBar();
				}

				@Override
				public void onException(@NonNull Exception e) {
					super.onException(e);
					mHomeBinding.mainControl.stopCaptureProgressBar();
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
		mHomeBinding.mainControl.startWebProgressBar();
		mDsRepository.onUri(uri, new AbstractDsSource.LoadedCallback() {
			@Override
			public void onVisionResponse(BatchAnnotateImagesResponse response) {
				super.onVisionResponse(response);
				mHomeBinding.mainControl.stopWebProgressBar();
				mHomeView.addResponseToScreen(response);
			}

			@Override
			public void onError(@NonNull Status status) {
				super.onError(status);
				mHomeBinding.mainControl.stopWebProgressBar();
			}

			@Override
			public void onException(@NonNull Exception e) {
				super.onException(e);
				mHomeView.showError(mHomeBinding.home, e.toString());
				mHomeBinding.mainControl.stopWebProgressBar();
			}
		});
	}

	@Override
	public void openLocal(@NonNull Context cxt, @NonNull Uri uri) {
		Cursor cursor = null;
		try {
			mHomeBinding.mainControl.startLocalProgressBar();
			String[] filePathColumn = { MediaStore.Images.Media.DATA };
			cursor = cxt.getContentResolver()
			            .query(uri, filePathColumn, null, null, null);
			if (cursor == null || cursor.getCount() < 1) {
				mHomeView.showError(mHomeBinding.home, cxt.getString(R.string.error_can_not_find_file));
				return;
			}
			cursor.moveToFirst();
			int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
			if (columnIndex < 0) {
				mHomeView.showError(mHomeBinding.home, cxt.getString(R.string.error_can_not_find_file));
				return;
			}
			mDsRepository.onFile(new File(cursor.getString(columnIndex)), new AbstractDsSource.LoadedCallback() {
				@Override
				public void onVisionResponse(BatchAnnotateImagesResponse response) {
					super.onVisionResponse(response);
					mHomeBinding.mainControl.stopLocalProgressBar();
					mHomeView.addResponseToScreen(response);
				}

				@Override
				public void onError(@NonNull Status status) {
					super.onError(status);
					mHomeBinding.mainControl.stopLocalProgressBar();
				}

				@Override
				public void onException(@NonNull Exception e) {
					super.onException(e);
					mHomeView.showError(mHomeBinding.home, e.toString());
					mHomeBinding.mainControl.stopLocalProgressBar();
				}
			});
		} catch (Exception e) {
			mHomeView.showError(mHomeBinding.home, cxt.getString(R.string.error_can_not_find_file));
			mHomeBinding.mainControl.stopLocalProgressBar();
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
	}

	public void testWiki() {
		mDsRepository.onKnowledgeQuery("上海", new AbstractDsSource.LoadedCallback() {
			@Override
			public void onKnowledgeResponse(WikiResult result) {
				super.onKnowledgeResponse(result);
			}
		});
	}
}
