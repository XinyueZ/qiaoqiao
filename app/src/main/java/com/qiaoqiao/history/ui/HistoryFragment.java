package com.qiaoqiao.history.ui;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.qiaoqiao.R;
import com.qiaoqiao.databinding.FragmentHistoryBinding;
import com.qiaoqiao.history.HistoryContract;
import com.qiaoqiao.history.HistoryManager;

public final class HistoryFragment extends Fragment implements HistoryContract.View {
	private static final int LAYOUT = R.layout.fragment_history;
	private FragmentHistoryBinding mBinding;
	private HistoryContract.Presenter mPresenter;

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
		mPresenter.begin();
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		mPresenter.stop();
	}

	@Override
	public void setPresenter(@NonNull HistoryManager presenter) {
		mPresenter = presenter;
	}

	@Override
	public FragmentHistoryBinding getBinding() {
		return mBinding;
	}
}
