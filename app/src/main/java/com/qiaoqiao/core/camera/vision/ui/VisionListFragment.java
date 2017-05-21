package com.qiaoqiao.core.camera.vision.ui;

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
import com.qiaoqiao.core.camera.vision.VisionContract;
import com.qiaoqiao.core.camera.vision.model.VisionEntity;
import com.qiaoqiao.databinding.FragmentListVisionBinding;

import java.util.ArrayList;
import java.util.List;

public final class VisionListFragment extends AbstractVisionFragment implements VisionContract.View<EntityAnnotation, EntityAnnotation, EntityAnnotation, WebEntity> {
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
		if (mPresenter != null) {
			mPresenter.loadRecent();
		}
	}


	@Override
	public void addEntities(@Nullable EntityAnnotation landmarkAnnotation, @Nullable EntityAnnotation logoAnnotation, @Nullable EntityAnnotation labelAnnotation, @Nullable WebEntity webEntity) {
		setRefreshing(false);
		if (landmarkAnnotation == null && logoAnnotation == null && labelAnnotation == null && webEntity == null) {
			return;
		}
		List<VisionEntity> filterList = new ArrayList<>();
		if (landmarkAnnotation != null) {
			filterList.add(new VisionEntity(landmarkAnnotation, "LANDMARK_DETECTION").setActivated(true));
		}
		if (logoAnnotation != null) {
			filterList.add(new VisionEntity(logoAnnotation, "LOGO_DETECTION").setActivated(true));
		}
		if (labelAnnotation != null) {
			filterList.add(new VisionEntity(labelAnnotation, "LABEL_DETECTION").setActivated(true));
		}
		if (webEntity != null) {
			filterList.add(new VisionEntity(webEntity, "WEB_DETECTION").setActivated(true));
		}
		mVisionListAdapter.addVisionEntityList(filterList);
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