package com.qiaoqiao.vision.ui;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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

import java.util.ArrayList;
import java.util.List;

public final class VisionMoreListFragment extends AbstractVisionFragment implements VisionContract.View<List<EntityAnnotation>, List<EntityAnnotation>, List<EntityAnnotation>, List<WebEntity>> {
	private static final int LAYOUT = R.layout.fragment_list_vision;
	private FragmentListVisionBinding mBinding;
	private VisionContract.Presenter mPresenter;
	private VisionListAdapter mVisionListAdapter;

	public static VisionMoreListFragment newInstance(@NonNull Context cxt) {
		return (VisionMoreListFragment) VisionMoreListFragment.instantiate(cxt, VisionMoreListFragment.class.getName());
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
		mBinding.visionRv.setLayoutManager(new GridLayoutManager(getActivity(), columns));
		mBinding.visionRv.setAdapter(mVisionListAdapter = new VisionListAdapter(columns));
		if (mPresenter != null) {
			mPresenter.loadRecent();
		}
	}


	@Override
	public void addEntities(@Nullable List<EntityAnnotation> landmarkAnnotations,
	                        @Nullable List<EntityAnnotation> logoAnnotations,
	                        @Nullable List<EntityAnnotation> labelAnnotations,
	                        @Nullable List<WebEntity> webEntities) {
		List<VisionEntity> output = new ArrayList<>();
		addEntities(landmarkAnnotations, "LANDMARK_DETECTION", output);
		addEntities(logoAnnotations, "LOGO_DETECTION", output);
		addEntities(labelAnnotations, "LABEL_DETECTION", output);
		addWebEntity(webEntities, output);

		mVisionListAdapter.addVisionEntityList(output);
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
	public void clean() {
		mVisionListAdapter.clean();
	}

	@Override
	public void setRefreshing(boolean refresh) {
		mBinding.loadingPb.setEnabled(refresh);
		mBinding.loadingPb.setRefreshing(refresh);
	}


	private void addEntities(@Nullable List<EntityAnnotation> entityAnnotationList, @NonNull String name, @NonNull List<VisionEntity> output) {
		if (entityAnnotationList == null || entityAnnotationList.size() <= 0) {
			return;
		}
		for (EntityAnnotation entityAnnotation : entityAnnotationList) {
			output.add(new VisionEntity(entityAnnotation, name, true));
		}

	}


	private void addWebEntity(@Nullable List<WebEntity> webEntityList, @NonNull List<VisionEntity> output) {
		if (webEntityList == null || webEntityList.size() <= 0) {
			setRefreshing(false);
			return;
		}
		for (WebEntity entityAnnotation : webEntityList) {
			output.add(new VisionEntity(entityAnnotation, "WEB_DETECTION", true));
		}
	}
}
