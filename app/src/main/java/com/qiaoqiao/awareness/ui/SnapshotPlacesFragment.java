package com.qiaoqiao.awareness.ui;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.qiaoqiao.R;
import com.qiaoqiao.awareness.AwarenessContract;
import com.qiaoqiao.databinding.PlacesBinding;

public final class SnapshotPlacesFragment extends Fragment implements AwarenessContract.View {

	private static final int LAYOUT = R.layout.fragment_snapshot_places;
	private AwarenessContract.Presenter mPresenter;
	private PlacesBinding mBinding;

	public static SnapshotPlacesFragment newInstance(@NonNull Context cxt) {
		return (SnapshotPlacesFragment) SnapshotPlacesFragment.instantiate(cxt, SnapshotPlacesFragment.class.getName());
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		mBinding = DataBindingUtil.inflate(inflater, LAYOUT, container, false);
		return mBinding.getRoot();
	}

	@Override
	public void setPresenter(@NonNull AwarenessContract.Presenter presenter) {
		mPresenter = presenter;
	}

	@Override
	public PlacesBinding getBinding() {
		return mBinding;
	}
}
