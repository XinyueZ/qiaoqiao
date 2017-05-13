package com.qiaoqiao.vision.ui;

import android.app.Activity;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.content.res.AppCompatResources;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.model.LatLng;
import com.google.api.services.vision.v1.model.EntityAnnotation;
import com.google.api.services.vision.v1.model.WebEntity;
import com.qiaoqiao.R;
import com.qiaoqiao.databinding.FragmentListVisionBinding;
import com.qiaoqiao.detail.ui.DetailActivity;
import com.qiaoqiao.location.ui.MapActivity;
import com.qiaoqiao.vision.VisionContract;
import com.qiaoqiao.vision.VisionPresenter;
import com.qiaoqiao.vision.model.VisionEntity;

public final class VisionListFragment extends Fragment implements VisionContract.View {
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

		mBinding.visionRv.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
		mBinding.visionRv.setHasFixedSize(true);
		mBinding.visionRv.setAdapter(mVisionListAdapter = new VisionListAdapter());
		final DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);
		final Drawable divideDrawable = AppCompatResources.getDrawable(getActivity(), R.drawable.divider_drawable);
		if (divideDrawable != null) {
			dividerItemDecoration.setDrawable(divideDrawable);
		}
		mBinding.visionRv.addItemDecoration(dividerItemDecoration);
	}


	@Override
	public void addLandmarkEntity(@NonNull EntityAnnotation entityAnnotation) {
		mVisionListAdapter.addVisionEntity(new VisionEntity(entityAnnotation, "LANDMARK_DETECTION"));
		mBinding.visionRv.scrollToPosition(mVisionListAdapter.getItemCount() - 1);
	}


	@Override
	public void addWebEntity(@NonNull WebEntity webEntity) {
		mVisionListAdapter.addVisionEntity(new VisionEntity(webEntity, "WEB_DETECTION"));
		mBinding.visionRv.scrollToPosition(mVisionListAdapter.getItemCount() - 1);
	}

	@Override
	public void setPresenter(@NonNull VisionPresenter presenter) {
		mPresenter = presenter;
	}

	@Override
	public FragmentListVisionBinding getBinding() {
		return mBinding;
	}

	@Override
	public void showDetail(@NonNull VisionEntity entity) {
		Activity activity = getActivity();
		if (activity == null) {
			return;
		}
		final LatLng latLng = entity.getLocation()
		                            .toLatLng();
		if (TextUtils.equals("LANDMARK_DETECTION", entity.getReadableName()) && latLng != null) {
			MapActivity.showInstance(activity, latLng);
			return;
		}
		final String descriptionText = entity.getDescription()
		                                     .getDescriptionText();
		if (TextUtils.equals("WEB_DETECTION", entity.getReadableName()) && !TextUtils.isEmpty(descriptionText)) {
			DetailActivity.showInstance(activity, descriptionText);
		}
	}
}
