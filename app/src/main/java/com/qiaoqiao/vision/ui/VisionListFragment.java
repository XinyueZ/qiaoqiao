package com.qiaoqiao.vision.ui;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.qiaoqiao.R;
import com.qiaoqiao.databinding.FragmentListVisionBinding;
import com.qiaoqiao.vision.VisionContract;
import com.qiaoqiao.vision.VisionManager;

public final class VisionListFragment extends Fragment implements VisionContract.View {
	private static final int LAYOUT = R.layout.fragment_list_vision;
	private FragmentListVisionBinding mBinding;
	private VisionContract.Presenter mPresenter;

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		mBinding = DataBindingUtil.inflate(inflater, LAYOUT, container, false);
		mBinding.setFragment(this);
		return mBinding.getRoot();
	}


	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		mPresenter.start();
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		mPresenter.stop();
	}

	@Override
	public void setPresenter(@NonNull VisionManager presenter) {
		mPresenter = presenter;
	}

	@Override
	public FragmentListVisionBinding getBinding() {
		return mBinding;
	}
}
