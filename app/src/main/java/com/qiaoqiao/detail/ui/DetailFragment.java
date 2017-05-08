package com.qiaoqiao.detail.ui;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.qiaoqiao.R;
import com.qiaoqiao.databinding.FragmentDetailBinding;
import com.qiaoqiao.detail.DetailContract;
import com.qiaoqiao.detail.DetailPresenter;

public final class DetailFragment extends Fragment implements DetailContract.View {
	private static final int LAYOUT = R.layout.fragment_detail;
	private DetailPresenter mPresenter;
	private FragmentDetailBinding mBinding;

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		mBinding = DataBindingUtil.inflate(inflater, LAYOUT, container, false);
		mBinding.setFragment(this);
		return mBinding.getRoot();
	}

	@Override
	public void setPresenter(@NonNull DetailPresenter presenter) {
		mPresenter = presenter;
	}

	@Override
	public FragmentDetailBinding getBinding() {
		return mBinding;
	}

	@Override
	public void onStart() {
		super.onStart();
		mPresenter.begin();
	}

	@Override
	public void onStop() {
		super.onStop();
		mPresenter.end();
	}
}
