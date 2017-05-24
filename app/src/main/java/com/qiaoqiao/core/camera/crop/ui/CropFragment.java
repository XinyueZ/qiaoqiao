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

import com.isseiaoki.simplecropview.CropImageView;
import com.isseiaoki.simplecropview.callback.CropCallback;
import com.qiaoqiao.R;
import com.qiaoqiao.core.camera.crop.CropContract;
import com.qiaoqiao.databinding.FragmentCropBinding;
import com.qiaoqiao.databinding.FragmentCropViewBinding;

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
	private CropViewFragment mCropViewFragment;

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
	public void onDestroyView() {
		mCropViewFragment = null;
		super.onDestroyView();
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
		getChildFragmentManager().beginTransaction()
		                         .replace(R.id.crop_container, mCropViewFragment = CropViewFragment.newInstance(getContext(), mData), CropViewFragment.class.getName())
		                         .commitNow();
		mCropViewFragment.setTargetFragment(this, 0x09);
	}


	@Override
	public void onClick(View view) {
		if (mCropViewFragment == null || !mCropViewFragment.isAdded()) {
			return;
		}
		mVibrator.vibrate(VIB_LNG);
		final Drawable drawable = mBinding.cropFbPb.getDrawable();
		if (drawable instanceof Animatable) {
			((Animatable) drawable).start();
			mBinding.cropFbPb.setVisibility(VISIBLE);
		}
		mCropViewFragment.crop();
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

	@Override
	public void rotate() {
		if (mCropViewFragment == null || !mCropViewFragment.isAdded()) {
			return;
		}
		mCropViewFragment.rotate();
	}

	public static final class CropViewFragment extends Fragment {
		private FragmentCropViewBinding mBinding;

		private static CropViewFragment newInstance(@NonNull Context cxt, @NonNull byte[] data) {
			Bundle args = new Bundle(1);
			args.putByteArray("data", data);
			return (CropViewFragment) CropViewFragment.instantiate(cxt, CropViewFragment.class.getName(), args);
		}

		@Nullable
		@Override
		public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
			mBinding = FragmentCropViewBinding.inflate(inflater, container, false);
			return mBinding.getRoot();
		}

		@Override
		public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
			super.onViewCreated(view, savedInstanceState);

			byte[] data = getArguments().getByteArray("data");
			mBinding.cropIv.setImageBitmap(BitmapFactory.decodeByteArray(data, 0, data.length));
			mBinding.cropIv.setCustomRatio(4, 3);
		}

		private void crop() {
			if (mBinding.cropIv.getCroppedBitmap() != null) {
				mBinding.cropIv.startCrop(Uri.fromFile(new File(getActivity().getCacheDir(), "cropped")), (CropCallback) getTargetFragment(), null);
			}
		}

		private void rotate() {
			mBinding.cropIv.rotateImage(CropImageView.RotateDegrees.ROTATE_90D);
		}
	}
}
