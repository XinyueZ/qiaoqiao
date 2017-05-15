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

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public final class VisionMoreListFragment extends AbstractVisionFragment implements VisionContract.View<List<EntityAnnotation>, List<WebEntity>> {
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
		mBinding.loadingPb.setColorSchemeResources(R.color.colorGreen, R.color.colorTeal, R.color.colorCyan);
		final int columns = getResources().getInteger(R.integer.num_columns);
		mBinding.visionRv.setLayoutManager(new GridLayoutManager(getActivity(), columns));
		mBinding.visionRv.setAdapter(mVisionListAdapter = new VisionListAdapter(columns));
		mPresenter.loadRecent();
	}


	@Override
	public void addLandmarkEntity(@NonNull List<EntityAnnotation> entityAnnotationList) {
		Observable.just(entityAnnotationList)
		          .subscribeOn(Schedulers.newThread())
		          .flatMap((Function<List<EntityAnnotation>, Observable<List<VisionEntity>>>) entityAnnotationList1 -> Observable.just(new ArrayList<VisionEntity>() {{
			          for (EntityAnnotation entityAnnotation : entityAnnotationList1) {
				          add(new VisionEntity(entityAnnotation, "LANDMARK_DETECTION", true));
			          }
		          }}))
		          .observeOn(AndroidSchedulers.mainThread())
		          .subscribe(list -> mVisionListAdapter.addVisionEntityList(list));
	}


	@Override
	public void addWebEntity(@NonNull List<WebEntity> webEntityList) {
		Observable.just(webEntityList)
		          .subscribeOn(Schedulers.newThread())
		          .flatMap((Function<List<WebEntity>, Observable<List<VisionEntity>>>) entityAnnotationList -> Observable.just(new ArrayList<VisionEntity>() {{
			          for (WebEntity entityAnnotation : entityAnnotationList) {
				          add(new VisionEntity(entityAnnotation, "WEB_DETECTION", true));
			          }
		          }}))
		          .observeOn(AndroidSchedulers.mainThread())
		          .subscribe(list -> {
			          mVisionListAdapter.addVisionEntityList(list);
			          Observable.just(list)
			                    .subscribeOn(Schedulers.newThread())
			                    .map(visionEntities -> {
				                    mPresenter.waitForImageUri(visionEntities);
				                    return visionEntities;
			                    })
			                    .observeOn(AndroidSchedulers.mainThread())
			                    .subscribe(list1 -> {
				                    mVisionListAdapter.notifyDataSetChanged();
				                    setRefreshing(false);
			                    });
		          });
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
	public void showDetail(@NonNull VisionEntity entity) {
		openDetail(entity);
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
