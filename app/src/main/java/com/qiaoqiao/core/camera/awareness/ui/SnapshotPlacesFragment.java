package com.qiaoqiao.core.camera.awareness.ui;

import android.Manifest;
import android.content.Context;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;
import com.qiaoqiao.R;
import com.qiaoqiao.core.camera.awareness.AwarenessContract;
import com.qiaoqiao.core.camera.awareness.map.ClusterManager;
import com.qiaoqiao.databinding.PlacesBinding;

import java.util.List;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public final class SnapshotPlacesFragment extends Fragment implements AwarenessContract.View,
                                                                      OnMapReadyCallback,
                                                                      GoogleMap.OnMapClickListener,
                                                                      SeekBar.OnSeekBarChangeListener {
	public static final int REQ_SETTING_LOCATING = 0x78;
	private static final int LAYOUT = R.layout.fragment_snapshot_places;
	private @Nullable AwarenessContract.Presenter mPresenter;
	private PlacesBinding mBinding;
	private @Nullable GoogleMap mGoogleMap;

	private int mThumbWidth;
	private int mThumbHeight;
	private int mTextColor;
	private int mThumbColor;

	public static SnapshotPlacesFragment newInstance(@NonNull Context cxt) {
		return (SnapshotPlacesFragment) SnapshotPlacesFragment.instantiate(cxt, SnapshotPlacesFragment.class.getName());
	}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Resources resources = getContext().getResources();
		mThumbWidth = resources.getDimensionPixelSize(R.dimen.seek_bar_thumb_size);
		mThumbHeight = resources.getDimensionPixelSize(R.dimen.seek_bar_thumb_size);
		mTextColor = ResourcesCompat.getColor(resources, R.color.colorYellow, null);
		mThumbColor = ResourcesCompat.getColor(resources, R.color.colorPrimaryDark, null);
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		mBinding = DataBindingUtil.inflate(inflater, LAYOUT, container, false);
		mBinding.setSeekBarListener(this);
		mBinding.locatingControl.setOnFromLocalClickedListener(v -> locating());
		mBinding.locatingControl.setOnAdjustClickedListener(v -> handleAdjustUI());

		return mBinding.getRoot();
	}

	private void handleAdjustUI() {
		mBinding.adjustFl.setVisibility(mBinding.adjustFl.getVisibility() != VISIBLE ?
		                                VISIBLE :
		                                GONE);
		if (mBinding.adjustFl.getVisibility() == VISIBLE && mPresenter != null) {
			final int progress = (int) mPresenter.loadGeosearchAdjust(getContext());
			mBinding.adjustRadiusSb.setProgress(progress);
			mBinding.adjustRadiusSb.setThumb(createThumbDrawable(progress));
		}
	}


	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		final SupportMapFragment fragmentById = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
		fragmentById.onCreate(savedInstanceState);
		fragmentById.onStart();
		fragmentById.getMapAsync(this);
		if (mPresenter != null) {
			mPresenter.settingLocating();
		}
	}


	@Override
	public void setPresenter(@NonNull AwarenessContract.Presenter presenter) {
		mPresenter = presenter;
	}

	@Override
	public PlacesBinding getBinding() {
		return mBinding;
	}


	@Override
	public void onLocatingError() {
		Toast.makeText(getActivity(), R.string.locating_fail, Toast.LENGTH_LONG)
		     .show();
		mBinding.locatingControl.stopLocalProgressBar();
	}


	public void onPlayServiceConnectionFailed() {
		Toast.makeText(getActivity(), R.string.play_service_fail, Toast.LENGTH_LONG)
		     .show();
	}


	@Override
	public void onMapReady(GoogleMap googleMap) {
		mGoogleMap = googleMap;
		mGoogleMap.setOnMapClickListener(this);
		mGoogleMap.getUiSettings()
		          .setMapToolbarEnabled(false);
		mGoogleMap.getUiSettings()
		          .setMyLocationButtonEnabled(false);
		if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(),
		                                                                                                                                                                          Manifest.permission
				                                                                                                                                                                          .ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
			return;
		}
		mGoogleMap.setMyLocationEnabled(true);
	}

	@Override
	public void locating() {
		mBinding.locatingControl.startLocalProgressBar();
		if (mPresenter != null) {
			mPresenter.locating(getContext());
		}
	}


	@Override
	public void onLocated(@NonNull LatLng latLng) {
		if (mGoogleMap != null) {
			if (isDetached()) {
				return;
			}
			if (!isAdded()) {
				return;
			}
			mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, getResources().getInteger(R.integer.zoom)));
		} else {
			final SupportMapFragment fragmentById = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
			fragmentById.getMapAsync(googleMap -> {
				mGoogleMap = googleMap;
				onLocated(latLng);
			});
		}
		if (mPresenter != null && getContext() != null) {
			mPresenter.searchAndSearch(getContext(), latLng);
		}
	}

	@Override
	public void solveSettingLocatingDialogProblem(@NonNull Status status) {
		try {
			status.startResolutionForResult(getActivity(), REQ_SETTING_LOCATING);
		} catch (IntentSender.SendIntentException e) {
			onLocatingError();
		}
	}


	@Override
	public void showAllGeoAndPlaces(@NonNull List<ClusterItem> clusterItemList) {
		if (mGoogleMap == null) {
			final SupportMapFragment fragmentById = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
			fragmentById.getMapAsync(googleMap -> {
				mGoogleMap = googleMap;
				showAllGeoAndPlaces(clusterItemList);
			});
			return;
		}
		ClusterManager.showGeosearch(getActivity(), mGoogleMap, clusterItemList);
		mBinding.locatingControl.stopLocalProgressBar();
		if (isDetached()) {
			return;
		}
		if (!isAdded()) {
			return;
		}
		mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(getResources().getInteger(R.integer.zoom) + 3));
	}

	@Override
	public void onMapClick(LatLng latLng) {
		dismissAdjust();
	}

	private void dismissAdjust() {
		if (mBinding.adjustFl.getVisibility() == VISIBLE) {
			mBinding.adjustFl.setVisibility(GONE);
			locating();
		}
	}

	private TextDrawable createThumbDrawable(long value) {
		return TextDrawable.builder()
		                   .beginConfig()
		                   .width(mThumbWidth)
		                   .height(mThumbHeight)
		                   .textColor(mTextColor)
		                   .endConfig()
		                   .buildRound(String.valueOf(value), mThumbColor);
	}

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
		mBinding.adjustRadiusSb.setThumb(createThumbDrawable(progress));
		if (mPresenter == null) {
			return;
		}
		mPresenter.setGeosearchRadius(getContext(), progress == 0 ? 500 : progress);
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {

	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {

	}
}
