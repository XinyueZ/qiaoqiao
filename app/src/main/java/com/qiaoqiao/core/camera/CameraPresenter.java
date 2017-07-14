package com.qiaoqiao.core.camera;


import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;

import com.google.api.services.vision.v1.model.BatchAnnotateImagesResponse;
import com.google.api.services.vision.v1.model.Status;
import com.qiaoqiao.R;
import com.qiaoqiao.core.camera.vision.VisionPresenter;
import com.qiaoqiao.repository.DsLoadedCallback;
import com.qiaoqiao.repository.DsRepository;

import java.io.File;

import javax.inject.Inject;

public final class CameraPresenter implements CameraContract.Presenter {
	private final @NonNull CameraContract.View mView;
	private final @NonNull DsRepository mDsRepository;
	private VisionPresenter mVisionPresenter;

	@Inject
	CameraPresenter(@NonNull CameraContract.View view, @NonNull DsRepository dsRepository) {
		mView = view;
		mDsRepository = dsRepository;
	}

	@Inject
	void onInjected() {
		mView.setPresenter(this);
	}

	public void setVisionPresenter(VisionPresenter visionPresenter) {
		mVisionPresenter = visionPresenter;
		visionPresenter.setCameraPresenter(this);
	}

	@Override
	public void begin(@NonNull FragmentActivity hostActivity) {
	}

	@Override
	public void end(@NonNull FragmentActivity hostActivity) {
	}


	@Override
	public void updateWhenResponse() {
		mView.updateViewWhenResponse();
	}

	@Override
	public void findAnnotateImages(@NonNull byte[] bytes) {
		mView.updateViewWhenRequest();
		mDsRepository.onBytes(bytes, new DsLoadedCallback() {
			@Override
			public void onVisionResponse(BatchAnnotateImagesResponse response) {
				super.onVisionResponse(response);
				mVisionPresenter.addResponseToScreen(response);
			}

			@Override
			public void onError(@NonNull Status status) {
				super.onError(status);
				updateWhenResponse();
			}

			@Override
			public void onException(@NonNull Exception e) {
				super.onException(e);
				mView.showError(e.toString());
				updateWhenResponse();
			}
		});
	}

	@Override
	public void openLink(@NonNull Uri uri) {
		mView.updateViewWhenRequest();
		mDsRepository.onUri(uri, new DsLoadedCallback() {
			@Override
			public void onVisionResponse(BatchAnnotateImagesResponse response) {
				super.onVisionResponse(response);
				mVisionPresenter.addResponseToScreen(response);
			}

			@Override
			public void onError(@NonNull Status status) {
				super.onError(status);
				updateWhenResponse();
			}

			@Override
			public void onException(@NonNull Exception e) {
				super.onException(e);
				mView.showError(e.toString());
				updateWhenResponse();
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
				mView.showError(cxt.getString(R.string.error_can_not_find_file));
				return;
			}
			cursor.moveToFirst();
			int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
			if (columnIndex < 0) {
				mView.showError(cxt.getString(R.string.error_can_not_find_file));
				return;
			}
			mView.updateViewWhenRequest();
			mDsRepository.onFile(new File(cursor.getString(columnIndex)), new DsLoadedCallback() {
				@Override
				public void onVisionResponse(BatchAnnotateImagesResponse response) {
					super.onVisionResponse(response);
					mVisionPresenter.addResponseToScreen(response);
				}

				@Override
				public void onError(@NonNull Status status) {
					super.onError(status);
					updateWhenResponse();
				}

				@Override
				public void onException(@NonNull Exception e) {
					super.onException(e);
					mView.showError(e.toString());
					updateWhenResponse();
				}
			});
		} catch (Exception e) {
			mView.showError(cxt.getString(R.string.error_can_not_find_file));
			updateWhenResponse();
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
	}
}
