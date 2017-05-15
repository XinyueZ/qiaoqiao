package com.qiaoqiao.vision.ui;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.qiaoqiao.R;
import com.qiaoqiao.databinding.LandmarkCellViewBinding;
import com.qiaoqiao.databinding.LandmarkViewBinding;
import com.qiaoqiao.databinding.WebCellViewBinding;
import com.qiaoqiao.databinding.WebViewBinding;
import com.qiaoqiao.utils.DeviceUtils;
import com.qiaoqiao.utils.LL;
import com.qiaoqiao.vision.bus.VisionEntityClickEvent;
import com.qiaoqiao.vision.model.VisionEntity;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

public final class VisionListAdapter extends RecyclerView.Adapter<VisionListAdapter.AbstractVisionViewHolder> {
	private static final int ITEM_LAYOUT_WEB = R.layout.item_vision_web;
	private static final int ITEM_LAYOUT_LANDMARK = R.layout.item_vision_landmark;
	private static final int ITEM_LAYOUT_WEB_CELL = R.layout.item_vision_web_cell;
	private static final int ITEM_LAYOUT_LANDMARK_CELL = R.layout.item_vision_landmark_cell;
	private static final int ITEM_TYPE_WEB = 0x91;
	private static final int ITEM_TYPE_LANDMARK = 0x92;
	private static final int ITEM_TYPE_WEB_CELL = 0x93;
	private static final int ITEM_TYPE_LANDMARK_CELL = 0x94;
	private int mColumns;
	private @NonNull final List<VisionEntity> mEntities = new ArrayList<>();

	public VisionListAdapter(int columns) {
		mColumns = columns;
	}

	public VisionListAdapter() {
	}

	public void clean() {
		mEntities.clear();
	}

	@Override
	public AbstractVisionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		Context cxt = parent.getContext();
		int size = 0;
		if (mColumns > 0) {
			size = DeviceUtils.getScreenSize(cxt).Width / mColumns;
		}
		switch (viewType) {
			case ITEM_TYPE_WEB:
				WebViewBinding webBinding = DataBindingUtil.bind(LayoutInflater.from(cxt)
				                                                               .inflate(ITEM_LAYOUT_WEB, parent, false));
				return new WebViewHolder(webBinding, mEntities, this);
			case ITEM_TYPE_LANDMARK:
				LandmarkViewBinding landmarkBinding = DataBindingUtil.bind(LayoutInflater.from(cxt)
				                                                                         .inflate(ITEM_LAYOUT_LANDMARK, parent, false));
				return new LandmarkViewHolder(landmarkBinding, mEntities, this);
			case ITEM_TYPE_WEB_CELL:
				WebCellViewBinding webCellBinding = DataBindingUtil.bind(LayoutInflater.from(cxt)
				                                                                       .inflate(ITEM_LAYOUT_WEB_CELL, parent, false));
				return new WebCellViewHolder(webCellBinding, mEntities, this, size);
			case ITEM_TYPE_LANDMARK_CELL:
			default:
				LandmarkCellViewBinding landmarkCellBinding = DataBindingUtil.bind(LayoutInflater.from(cxt)
				                                                                                 .inflate(ITEM_LAYOUT_LANDMARK_CELL, parent, false));
				return new LandmarkCellViewHolder(landmarkCellBinding, mEntities, this, size);
		}
	}

	@Override
	public void onBindViewHolder(AbstractVisionViewHolder holder, int position) {
		if (holder == null) {
			return;
		}
		holder.onBindViewHolder();
		holder.mBinding.executePendingBindings();
	}

	@Override
	public void onViewRecycled(AbstractVisionViewHolder holder) {
		holder.onViewRecycled();
	}


	public void addVisionEntity(@NonNull VisionEntity entity) {
		LL.d(entity.toString());
		mEntities.add(entity);
		//TODO Should update the position where we insert only.
		notifyDataSetChanged();
	}

	public void addVisionEntityList(@NonNull List<VisionEntity> entityList) {
		mEntities.addAll(entityList);
		//TODO Should update the position where we insert only.
		notifyDataSetChanged();
	}

	@Override
	public int getItemCount() {
		return mEntities.size();
	}

	@Override
	public int getItemViewType(int position) {
		VisionEntity entity = mEntities.get(position);
		if (TextUtils.equals(entity.getReadableName(), "WEB_DETECTION")) {
			return entity.isInCell() ?
			       ITEM_TYPE_WEB_CELL :
			       ITEM_TYPE_WEB;
		}
		if (TextUtils.equals(entity.getReadableName(), "LANDMARK_DETECTION")) {
			return entity.isInCell() ?
			       ITEM_TYPE_LANDMARK_CELL :
			       ITEM_TYPE_LANDMARK;
		}
		return super.getItemViewType(position);
	}


	static abstract class AbstractVisionViewHolder extends RecyclerView.ViewHolder {
		private final @NonNull ViewDataBinding mBinding;
		final @NonNull VisionEntityClickEvent mVisionEntityClickEvent = new VisionEntityClickEvent();
		final @NonNull List<VisionEntity> mEntities;
		private final @NonNull VisionListAdapter mVisionListAdapter;

		private AbstractVisionViewHolder(@NonNull ViewDataBinding binding, @NonNull List<VisionEntity> entities, @NonNull VisionListAdapter adapter) {
			super(binding.getRoot());
			mEntities = entities;
			mBinding = binding;
			mVisionListAdapter = adapter;
		}

		public void onClicked(VisionEntity visionEntity) {
			mVisionEntityClickEvent.setEntity(visionEntity);
			EventBus.getDefault()
			        .post(mVisionEntityClickEvent);
		}

		abstract void onBindViewHolder();

		abstract void onViewRecycled();
	}

	public final static class WebViewHolder extends AbstractVisionViewHolder {
		private final @NonNull WebViewBinding mItemVisionWebBinding;

		private WebViewHolder(@NonNull WebViewBinding binding, @NonNull List<VisionEntity> entities, @NonNull VisionListAdapter adapter) {
			super(binding, entities, adapter);
			mItemVisionWebBinding = binding;
		}

		@Override
		void onBindViewHolder() {
			VisionEntity entity = mEntities.get(getAdapterPosition());
			mItemVisionWebBinding.visionTv.setText(entity.getDescription()
			                                             .getDescriptionText());
			mItemVisionWebBinding.setVisionEntity(entity);
			mItemVisionWebBinding.setViewholder(this);
		}

		@Override
		void onViewRecycled() {

		}
	}

	public final static class LandmarkViewHolder extends AbstractVisionViewHolder {
		private final @NonNull LandmarkViewBinding mItemVisionLandmarkBinding;

		private LandmarkViewHolder(@NonNull LandmarkViewBinding binding, @NonNull List<VisionEntity> entries, @NonNull VisionListAdapter adapter) {
			super(binding, entries, adapter);
			mItemVisionLandmarkBinding = binding;
		}

		@Override
		void onBindViewHolder() {
			VisionEntity entity = mEntities.get(getAdapterPosition());
			mItemVisionLandmarkBinding.visionTv.setText(entity.getDescription()
			                                                  .getDescriptionText());
			mItemVisionLandmarkBinding.setVisionEntity(entity);
			mItemVisionLandmarkBinding.setViewholder(this);
		}

		@Override
		void onViewRecycled() {

		}
	}


	public final static class WebCellViewHolder extends AbstractVisionViewHolder {
		private final @NonNull WebCellViewBinding mItemWebCellBinding;
		private int mSize;

		private WebCellViewHolder(@NonNull WebCellViewBinding binding, @NonNull List<VisionEntity> entities, @NonNull VisionListAdapter adapter, int size) {
			super(binding, entities, adapter);
			mItemWebCellBinding = binding;
			mSize = size;
		}


		private static void loadImage(Context cxt, VisionEntity entity, ImageView imageView) {
			if (entity.getImageUri() != Uri.EMPTY) {
				Glide.with(cxt)
				     .load(entity.getImageUri())
				     .centerCrop()
				     .placeholder(R.drawable.ic_default_image)
				     .crossFade()
				     .into(imageView);
			}
		}

		@Override
		void onBindViewHolder() {
			VisionEntity entity = mEntities.get(getAdapterPosition());
			mItemWebCellBinding.visionTv.setText(entity.getDescription()
			                                           .getDescriptionText());
			mItemWebCellBinding.setVisionEntity(entity);
			mItemWebCellBinding.setViewholder(this);

			mItemWebCellBinding.visionIv.getLayoutParams().width = mSize;
			mItemWebCellBinding.visionIv.getLayoutParams().height = mSize;

			loadImage(itemView.getContext(), entity, mItemWebCellBinding.visionIv);
		}

		@Override
		void onViewRecycled() {

		}
	}

	public final static class LandmarkCellViewHolder extends AbstractVisionViewHolder implements OnMapReadyCallback,
	                                                                                             GoogleMap.OnMapClickListener {
		private final @NonNull LandmarkCellViewBinding mItemLandmarkCellBinding;
		private int mSize;
		private @Nullable GoogleMap mGoogleMap;

		private LandmarkCellViewHolder(@NonNull LandmarkCellViewBinding binding, @NonNull List<VisionEntity> entries, @NonNull VisionListAdapter adapter, int size) {
			super(binding, entries, adapter);
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
			LatLng pin = mEntities.get(getAdapterPosition())
			                      .getLocation()
			                      .toLatLng();
			googleMap.addMarker(new MarkerOptions().position(pin)
			                                       .draggable(true));
			googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(pin, 16));
			googleMap.setOnMapClickListener(this);
		}

		@Override
		void onBindViewHolder() {
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
		void onViewRecycled() {
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
	}
}
