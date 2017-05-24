package com.qiaoqiao.core.camera.crop.ui;


import android.content.Context;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.isseiaoki.simplecropview.callback.CropCallback;
import com.qiaoqiao.core.camera.crop.CropContract;
import com.qiaoqiao.databinding.FragmentCropBinding;

import org.apache.commons.io.output.ByteArrayOutputStream;

import java.io.File;

import static android.view.View.VISIBLE;

public final class CropFragment extends Fragment implements CropContract.View,
                                                            View.OnClickListener,
                                                            CropCallback {

	private static final int VIB_LNG = 50;
	private Vibrator mVibrator;
	private FragmentCropBinding mBinding;
	private CropContract.Presenter mPresenter;
	private byte[] mData;

	public static CropFragment newInstance(@NonNull Context cxt) {
		return (CropFragment) CropFragment.instantiate(cxt, CropFragment.class.getName());
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		mVibrator = (Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);
		mBinding = FragmentCropBinding.inflate(inflater, container, false);
		mBinding.cropFb.setOnClickListener(this);
		return mBinding.getRoot();
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		showImage();
	}


	@Override
	public void setPresenter(@NonNull CropContract.Presenter presenter) {
		mPresenter = presenter;
	}

	public FragmentCropBinding getBinding() {
		return mBinding;
	}

	@Override
	public void setImageData(@NonNull byte[] data) {
		mData = data;
	}

	private void showImage() {
		if (mData == null || mData.length <= 0) {
			return;
		}
		mBinding.cropIv.setImageBitmap(BitmapFactory.decodeByteArray(mData, 0, mData.length));
		mBinding.cropIv.setCustomRatio(4, 3);
	}


	@Override
	public void onClick(View view) {
		mVibrator.vibrate(VIB_LNG);
		if (mBinding.cropIv.getCroppedBitmap() != null) {
			final Drawable drawable = mBinding.cropFbPb.getDrawable();
			if (drawable instanceof Animatable) {
				((Animatable) drawable).start();
				mBinding.cropFbPb.setVisibility(VISIBLE);
			}
			mBinding.cropIv.startCrop(Uri.fromFile(new File(getActivity().getCacheDir(), "cropped")), this, null);
		}
	}

	@Override
	public void onSuccess(Bitmap bitmap) {
		//Cropped
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
		byte[] byteArray = stream.toByteArray();
		mPresenter.cropped(byteArray);
		if (!bitmap.isRecycled()) {
			bitmap.recycle();
		}
	}


	@Override
	public void onError() {
		mPresenter.croppedFail();
	}
}
