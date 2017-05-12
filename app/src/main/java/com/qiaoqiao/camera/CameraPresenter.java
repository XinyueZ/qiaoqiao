package com.qiaoqiao.camera;


import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;

import com.google.android.cameraview.CameraView;
import com.google.api.services.vision.v1.model.BatchAnnotateImagesResponse;
import com.google.api.services.vision.v1.model.Status;
import com.qiaoqiao.R;
import com.qiaoqiao.ds.DsLoadedCallback;
import com.qiaoqiao.ds.DsRepository;
import com.qiaoqiao.utils.LL;

import java.io.File;

import javax.inject.Inject;

public final class CameraPresenter implements CameraContract.Presenter {
	private final @NonNull CameraContract.View mView;
	private final @NonNull DsRepository mDsRepository;

	@Inject
	CameraPresenter(@NonNull CameraContract.View view, @NonNull DsRepository dsRepository) {
		mView = view;
		mDsRepository = dsRepository;
	}

	@Inject
	void onInjected() {
		mView.setPresenter(this);
		mView.getBinding().mainControl.setOnFromLocalClickedListener(v -> {
			mView.showLoadFromLocal();
			mView.hideSystemUi();
		});
		mView.getBinding().mainControl.setOnCaptureClickedListener(v -> {
			mView.capturePhoto();
			mView.hideSystemUi();
		});
		mView.getBinding().mainControl.setOnFromWebClickedListener(v -> {
			mView.showInputFromWeb();
			mView.hideSystemUi();
		});
	}

	@Override
	public void begin() {
		mView.cameraBegin(mCameraCallback);
		mView.showMainControl();
	}

	@Override
	public void stop() {
		mView.cameraEnd(mCameraCallback);
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
					mView.addResponseToScreen(response);
					mView.updateUIWhenGetPhoto();
				}

				@Override
				public void onError(@NonNull Status status) {
					super.onError(status);
					mView.updateUIWhenGetPhoto();
				}

				@Override
				public void onException(@NonNull Exception e) {
					super.onException(e);
					mView.updateUIWhenGetPhoto();
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
		mView.openLink();
		mDsRepository.onUri(uri, new DsLoadedCallback() {
			@Override
			public void onVisionResponse(BatchAnnotateImagesResponse response) {
				super.onVisionResponse(response);
				mView.addResponseToScreen(response);
				mView.updateUIWhenGetPhoto();
			}

			@Override
			public void onError(@NonNull Status status) {
				super.onError(status);
				mView.updateUIWhenGetPhoto();
			}

			@Override
			public void onException(@NonNull Exception e) {
				super.onException(e);
				mView.showError(e.toString());
				mView.updateUIWhenGetPhoto();
			}
		});
	}

	@Override
	public void openLocal(@NonNull Context cxt, @NonNull Uri uri) {
		Cursor cursor = null;
		try {
			mView.openLocal();
			String[] filePathColumn = { MediaStore.Images.Media.DATA };
			cursor = cxt.getContentResolver()
			            .query(uri, filePathColumn, null, null, null);
			if (cursor == null || cursor.getCount() < 1) {
				mView.showError(cxt.getString(R.string.error_can_not_find_file));
				return;
			}
			cursor.moveToFirst();
			int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
			if (columnIndex < 0) {
				mView.showError(cxt.getString(R.string.error_can_not_find_file));
				return;
			}
			mDsRepository.onFile(new File(cursor.getString(columnIndex)), new DsLoadedCallback() {
				@Override
				public void onVisionResponse(BatchAnnotateImagesResponse response) {
					super.onVisionResponse(response);
					mView.addResponseToScreen(response);
					mView.updateUIWhenGetPhoto();
				}

				@Override
				public void onError(@NonNull Status status) {
					super.onError(status);
					mView.updateUIWhenGetPhoto();
				}

				@Override
				public void onException(@NonNull Exception e) {
					super.onException(e);
					mView.showError(e.toString());
					mView.updateUIWhenGetPhoto();
				}
			});
		} catch (Exception e) {
			mView.showError(cxt.getString(R.string.error_can_not_find_file));
			mView.updateUIWhenGetPhoto();
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
	}
}
