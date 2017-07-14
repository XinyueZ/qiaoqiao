package com.qiaoqiao.core.camera.crop.ui;


import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.qiaoqiao.R;
import com.qiaoqiao.core.camera.crop.CropContract;
import com.qiaoqiao.core.camera.crop.model.CropSource;
import com.qiaoqiao.databinding.FragmentCropBinding;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.apache.commons.io.output.ByteArrayOutputStream;

import static android.view.View.VISIBLE;

public final class CropFragment extends Fragment implements CropContract.View,
                                                            View.OnClickListener,
                                                            CropImageView.OnCropImageCompleteListener {

	private static final int VIB_LNG = 50;
	private Vibrator mVibrator;
	private FragmentCropBinding mBinding;
	private @Nullable CropContract.Presenter mPresenter;
	private @Nullable byte[] mData;
	private Uri mUri = Uri.EMPTY;

	public static CropFragment newInstance(@NonNull Context cxt) {
		return (CropFragment) CropFragment.instantiate(cxt, CropFragment.class.getName());
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		mVibrator = (Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);
		mBinding = FragmentCropBinding.inflate(inflater, container, false);
		mBinding.cropFb.setOnClickListener(this);
		mBinding.cropIv.setOnCropImageCompleteListener(this);
		return mBinding.getRoot();
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		setupCropImageView();
		showImage();
		mBinding.cropRotateBtn.setOnClickListener(this);
	}


	@Override
	public void setPresenter(@NonNull CropContract.Presenter presenter) {
		mPresenter = presenter;
	}

	private void setupCropImageView() {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
		String aspectRatio = prefs.getString(getString(R.string.preference_key_camera_aspect_ratio), "4:3");
		String[] aspectRatioXY = aspectRatio.split(":");
		int x = Integer.parseInt(aspectRatioXY[0]);
		int y = Integer.parseInt(aspectRatioXY[1]);
		mBinding.cropIv.setAspectRatio(x, y);
	}

	@Override
	public FragmentCropBinding getBinding() {
		return mBinding;
	}


	@Override
	public void setCropSource(@NonNull CropSource cropSource) {
		mUri = cropSource.getUri();
		mData = cropSource.getData();
	}

	private void showImage() {
		if (mData == null) {
			mBinding.cropIv.setImageUriAsync(mUri);
			return;
		}
		if (mData.length <= 0) {
			return;
		}
		mBinding.cropIv.setImageBitmap(BitmapFactory.decodeByteArray(mData, 0, mData.length));
	}


	@Override
	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.crop_rotate_btn:
				rotate();
				break;
			default:
				mVibrator.vibrate(VIB_LNG);
				final Drawable drawable = mBinding.cropFbPb.getDrawable();
				if (drawable instanceof Animatable) {
					((Animatable) drawable).start();
					mBinding.cropFbPb.setVisibility(VISIBLE);
				}
				mBinding.cropIv.getCroppedImageAsync();
				mBinding.cropFb.setEnabled(false);
				break;
		}
	}

	@Override
	public void rotate() {
		mBinding.cropIv.rotateImage(90);
	}

	@Override
	public void onCropImageComplete(CropImageView cropImageView, CropImageView.CropResult cropResult) {
		if (cropResult.isSuccessful()) {
			onSuccess(cropResult.getBitmap());
		} else {
			onError();
		}
	}

	private void onSuccess(Bitmap bitmap) {
		//Cropped
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
		byte[] byteArray = stream.toByteArray();
		if (mPresenter != null) {
			mPresenter.cropped(byteArray);
		}
		if (!bitmap.isRecycled()) {
			bitmap.recycle();
		}
		mBinding.cropFb.setEnabled(true);
	}


	private void onError() {
		if (mPresenter != null) {
			mPresenter.croppedFail();
			mBinding.cropFb.setEnabled(true);
		}
	}
}
