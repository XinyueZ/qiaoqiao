package com.qiaoqiao.core.camera.crop.ui;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.isseiaoki.simplecropview.CropImageView;
import com.qiaoqiao.core.camera.crop.CropContract;
import com.qiaoqiao.databinding.FragmentCropBinding;

import static com.qiaoqiao.utils.ImageUtils.convertBytes;

public final class CropFragment extends Fragment implements CropContract.View {

	private FragmentCropBinding mBinding;
	private CropContract.Presenter mPresenter;
	private byte[] mData;

	public static CropFragment newInstance(@NonNull Context cxt) {
		return (CropFragment) CropFragment.instantiate(cxt, CropFragment.class.getName());
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		mBinding = FragmentCropBinding.inflate(inflater, container, false);
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
	public void setImageData(byte[] data) {
		mData = data;
	}

	private void showImage() {
		if (mData == null || mData.length <= 0) {
			return;
		}
		byte[] compressed = convertBytes(mData);
		final Bitmap bitmap = BitmapFactory.decodeByteArray(compressed, 0, compressed.length);
		mBinding.cropIv.setImageBitmap(bitmap);
		mBinding.cropIv.setCustomRatio(4, 3);
		mBinding.cropIv.rotateImage(CropImageView.RotateDegrees.ROTATE_90D);
	}


}
