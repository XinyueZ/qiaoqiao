package com.qiaoqiao.vision.ui;

import android.content.Context;
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

import com.google.api.services.vision.v1.model.EntityAnnotation;
import com.google.api.services.vision.v1.model.WebEntity;
import com.qiaoqiao.R;
import com.qiaoqiao.databinding.FragmentListVisionBinding;
import com.qiaoqiao.vision.VisionContract;
import com.qiaoqiao.vision.model.VisionEntity;

public final class VisionListFragment extends AbstractVisionFragment implements VisionContract.View<EntityAnnotation, WebEntity> {
	private static final int LAYOUT = R.layout.fragment_list_vision;
	private FragmentListVisionBinding mBinding;
	private VisionContract.Presenter mPresenter;

	private VisionListAdapter mVisionListAdapter;

	public static VisionListFragment newInstance(@NonNull Context cxt) {
		return (VisionListFragment) VisionListFragment.instantiate(cxt, VisionListFragment.class.getName());
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
		mBinding.loadingPb.setColorSchemeResources(R.color.colorPrimary, R.color.colorPrimaryDark, R.color.colorAccent);
		final int columns = getResources().getInteger(R.integer.num_columns);
		final GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), columns);
		mBinding.visionRv.setLayoutManager(layoutManager);
		mBinding.visionRv.setAdapter(mVisionListAdapter = new VisionListAdapter());
		final DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);
		final Drawable divideDrawable = AppCompatResources.getDrawable(getActivity(), R.drawable.divider_drawable);
		if (divideDrawable != null) {
			dividerItemDecoration.setDrawable(divideDrawable);
		}
		mBinding.visionRv.addItemDecoration(dividerItemDecoration);
		mPresenter.loadRecent();
	}


	@Override
	public void addEntity(@Nullable EntityAnnotation entityAnnotation, @Nullable WebEntity webEntity) {
		setRefreshing(false);
		if (entityAnnotation == null && webEntity == null) {
			return;
		}
		if (entityAnnotation == null) {
			mVisionListAdapter.addVisionEntityArray(new VisionEntity(webEntity, "WEB_DETECTION").setActivated(true));
		} else if (webEntity == null) {
			mVisionListAdapter.addVisionEntityArray(new VisionEntity(entityAnnotation, "LANDMARK_DETECTION").setActivated(true));
		} else {
			mVisionListAdapter.addVisionEntityArray(new VisionEntity(entityAnnotation, "LANDMARK_DETECTION").setActivated(true), new VisionEntity(webEntity, "WEB_DETECTION").setActivated(true));
		}

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
	public void clean() {
		mVisionListAdapter.clean();
	}

	@Override
	public void setRefreshing(boolean refresh) {
		mBinding.loadingPb.setEnabled(refresh);
		mBinding.loadingPb.setRefreshing(refresh);
	}
}
