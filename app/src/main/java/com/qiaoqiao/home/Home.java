package com.qiaoqiao.home;


import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Toast;

import com.qiaoqiao.databinding.HomeBinding;
import com.qiaoqiao.views.MainControl;

import javax.inject.Inject;

public final class Home {
	private final HomeBinding mBinding;

	@Inject
	public Home(@NonNull HomeBinding binding) {
		mBinding = binding;
		mBinding.mainControl.setOnFromLocalClickedListener(new MainControl.OnFromLocalClickedListener() {
			@Override
			public void onClick(View v) {
				Toast.makeText(v.getContext(), "setOnFromLocalClickedListener", Toast.LENGTH_SHORT)
				     .show();
				mBinding.getUiHelper().hide();
				mBinding.loadingPb.setVisibility(View.VISIBLE);
			}
		});
		mBinding.mainControl.setOnCaptureClickedListener(new MainControl.OnCaptureClickedListener() {
			@Override
			public void onClick(View v) {
				Toast.makeText(v.getContext(), "setOnCaptureClickedListener", Toast.LENGTH_SHORT)
				     .show();

				mBinding.getUiHelper().hide();
				mBinding.loadingPb.setVisibility(View.VISIBLE);
			}
		});
		mBinding.mainControl.setOnFromWebClickedListener(new MainControl.OnFromWebClickedListener() {
			@Override
			public void onClick(View v) {
				Toast.makeText(v.getContext(), "setOnFromWebClickedListener", Toast.LENGTH_SHORT)
				     .show();

				mBinding.getUiHelper().hide();
				mBinding.loadingPb.setVisibility(View.VISIBLE);
			}
		});
	}
}
