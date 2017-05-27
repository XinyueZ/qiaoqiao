package com.qiaoqiao.core.camera.vision.ui;

import android.content.Context;
import android.content.res.Configuration;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.content.res.AppCompatResources;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.qiaoqiao.R;
import com.qiaoqiao.app.Key;
import com.qiaoqiao.core.camera.vision.VisionContract;
import com.qiaoqiao.core.camera.vision.model.VisionEntity;
import com.qiaoqiao.databinding.FragmentListVisionBinding;

import java.util.List;

public final class VisionListFragment extends AbstractVisionFragment implements VisionContract.View {
	private static final int LAYOUT = R.layout.fragment_list_vision;
	private static final String EXTRAS_KEY = VisionListFragment.class.getName() + ".EXTRAS.key";
	private FragmentListVisionBinding mBinding;
	private @Nullable VisionContract.Presenter mPresenter;

	private @Nullable VisionListAdapter mVisionListAdapter;

	public static VisionListFragment newInstance(@NonNull Context cxt, @NonNull Key key) {
		Bundle args = new Bundle(1);
		args.putSerializable(EXTRAS_KEY, key);
		return (VisionListFragment) VisionListFragment.instantiate(cxt, VisionListFragment.class.getName(), args);
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		mBinding = DataBindingUtil.inflate(inflater, LAYOUT, container, false);
		mBinding.setFragment(this);
		return mBinding.getRoot();
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		setRefreshing(false);

		Key key = (Key) getArguments().getSerializable(EXTRAS_KEY);
		if (key == null) {
			return;
		}
		mBinding.loadingPb.setColorSchemeResources(R.color.colorPrimary, R.color.colorPrimaryDark, R.color.colorAccent);
		final int columns = getResources().getInteger(R.integer.num_columns);
		final GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), columns);
		mBinding.visionRv.setLayoutManager(layoutManager);
		mBinding.visionRv.setAdapter(mVisionListAdapter = new VisionListAdapter(key));
		final DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);
		final Drawable divideDrawable = AppCompatResources.getDrawable(getActivity(), R.drawable.divider_drawable);
		if (divideDrawable != null) {
			dividerItemDecoration.setDrawable(divideDrawable);
		}
		mBinding.visionRv.addItemDecoration(dividerItemDecoration);
		if (mPresenter != null) {
			mPresenter.loadRecent();
		}
	}


	@Override
	public void addEntities(@NonNull List<VisionEntity> visionEntityList) {
		if (mVisionListAdapter == null) {
			return;
		}
		mVisionListAdapter.addVisionEntityList(visionEntityList);
		setRefreshing(false);
	}

	@Override
	public void setPresenter(@NonNull VisionContract.Presenter presenter) {
		mPresenter = presenter;
	}

	@Override
	public FragmentListVisionBinding getBinding() {
		return mBinding;
	}

	@Override
	public void showDetail(@NonNull VisionEntity entity, View transitionView) {
		openDetail(entity, transitionView);
	}


	@Override
	public void setRefreshing(boolean refresh) {
		mBinding.loadingPb.setEnabled(refresh);
		mBinding.loadingPb.setRefreshing(refresh);
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);

		final int columns = getResources().getInteger(R.integer.num_columns);
		mBinding.visionRv.setLayoutManager(new GridLayoutManager(getActivity(), columns));
		mBinding.visionRv.getAdapter()
		                 .notifyDataSetChanged();
	}


	@Override
	public void clear() {
		if (mVisionListAdapter != null) {
			mVisionListAdapter.clear();
		}
	}
}
