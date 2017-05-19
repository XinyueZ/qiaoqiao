package com.qiaoqiao.core.camera.vision.ui.viewholder;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.qiaoqiao.databinding.LandmarkCellViewBinding;
import com.qiaoqiao.core.camera.vision.model.VisionEntity;

import java.util.List;

public final class LandmarkCellViewHolder extends AbstractVisionViewHolder implements OnMapReadyCallback,
                                                                                      GoogleMap.OnMapClickListener {
	private final @NonNull LandmarkCellViewBinding mItemLandmarkCellBinding;
	private final int mSize;
	private @Nullable GoogleMap mGoogleMap;

	public LandmarkCellViewHolder(@NonNull LandmarkCellViewBinding binding, @NonNull List<VisionEntity> entries, int size) {
		super(binding, entries);
		mItemLandmarkCellBinding = binding;
		mSize = size;
	}

	@Override
	public void onMapReady(GoogleMap googleMap) {
		mGoogleMap = googleMap;
		googleMap.getUiSettings()
		         .setMapToolbarEnabled(false);
		googleMap.getUiSettings()
		         .setScrollGesturesEnabled(false);
		if (getAdapterPosition() >= 0 && getAdapterPosition() < mEntities.size()) {
			LatLng pin = mEntities.get(getAdapterPosition())
			                      .getLocation()
			                      .toLatLng();
			if (pin != null) {
				googleMap.addMarker(new MarkerOptions().position(pin)
				                                       .draggable(true));
				googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(pin, 16));
			}
		}
		googleMap.setOnMapClickListener(this);
	}

	@Override
	public void onBindViewHolder() {
		VisionEntity entity = mEntities.get(getAdapterPosition());
		mItemLandmarkCellBinding.visionTv.setText(entity.getDescription()
		                                                .getDescriptionText());
		mItemLandmarkCellBinding.setVisionEntity(entity);
		mItemLandmarkCellBinding.setViewholder(this);

		mItemLandmarkCellBinding.itemMapview.getLayoutParams().width = mSize;
		mItemLandmarkCellBinding.itemMapview.getLayoutParams().height = mSize;
		mItemLandmarkCellBinding.itemMapview.onCreate(null);
		mItemLandmarkCellBinding.itemMapview.onStart();
		mItemLandmarkCellBinding.itemMapview.onResume();
		mItemLandmarkCellBinding.itemMapview.getMapAsync(this);
	}

	@Override
	public void onViewRecycled() {
		if (mGoogleMap != null) {
			mGoogleMap.clear();
			mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NONE);

			mItemLandmarkCellBinding.itemMapview.onPause();
			mItemLandmarkCellBinding.itemMapview.onStop();
			mItemLandmarkCellBinding.itemMapview.onDestroy();
		}
	}

	@Override
	public void onMapClick(LatLng latLng) {
		onClicked(mEntities.get(getAdapterPosition()));
	}

	@Override
	protected View getTransitionView() {
		return mItemLandmarkCellBinding.itemMapview;
	}
}
