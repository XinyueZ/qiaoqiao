package com.qiaoqiao.vision.ui;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.qiaoqiao.R;
import com.qiaoqiao.databinding.LandmarkCellViewBinding;
import com.qiaoqiao.databinding.LandmarkViewBinding;
import com.qiaoqiao.databinding.WebCellViewBinding;
import com.qiaoqiao.databinding.WebViewBinding;
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
	private @NonNull final List<VisionEntity> mEntities = new ArrayList<>();

	@Override
	public AbstractVisionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		Context cxt = parent.getContext();
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
				return new WebCellViewHolder(webCellBinding, mEntities, this);
			case ITEM_TYPE_LANDMARK_CELL:
			default:
				LandmarkCellViewBinding landmarkCellBinding = DataBindingUtil.bind(LayoutInflater.from(cxt)
				                                                                         .inflate(ITEM_LAYOUT_LANDMARK_CELL, parent, false));
				return new LandmarkCellViewHolder(landmarkCellBinding, mEntities, this);
		}
	}

	@Override
	public void onBindViewHolder(AbstractVisionViewHolder holder, int position) {
		if (holder == null) {
			return;
		}
		VisionEntity entity = mEntities.get(position);
		switch (getItemViewType(position)) {
			case ITEM_TYPE_WEB:
				WebViewHolder webViewHolder = (WebViewHolder) holder;
				webViewHolder.mItemVisionWebBinding.visionTv.setText(entity.getDescription()
				                                                           .getDescriptionText());
				webViewHolder.mItemVisionWebBinding.setVisionEntity(entity);
				webViewHolder.mItemVisionWebBinding.setViewholder(webViewHolder);
				break;
			case ITEM_TYPE_LANDMARK:
				LandmarkViewHolder landmarkViewHolder = (LandmarkViewHolder) holder;
				landmarkViewHolder.mItemVisionLandmarkBinding.visionTv.setText(entity.getDescription()
				                                                                     .getDescriptionText());
				landmarkViewHolder.mItemVisionLandmarkBinding.setVisionEntity(entity);
				landmarkViewHolder.mItemVisionLandmarkBinding.setViewholder(landmarkViewHolder);
				break;
			case ITEM_TYPE_WEB_CELL:
				WebCellViewHolder webCellViewHolder = (WebCellViewHolder) holder;
				webCellViewHolder.mItemWebCellBinding.visionTv.setText(entity.getDescription()
				                                                             .getDescriptionText());
				webCellViewHolder.mItemWebCellBinding.setVisionEntity(entity);
				webCellViewHolder.mItemWebCellBinding.setViewholder(webCellViewHolder);
				break;
			case ITEM_TYPE_LANDMARK_CELL:
				LandmarkCellViewHolder landmarkCellViewHolder = (LandmarkCellViewHolder) holder;
				landmarkCellViewHolder.mItemLandmarkCellBinding.visionTv.setText(entity.getDescription()
				                                                                       .getDescriptionText());
				landmarkCellViewHolder.mItemLandmarkCellBinding.setVisionEntity(entity);
				landmarkCellViewHolder.mItemLandmarkCellBinding.setViewholder(landmarkCellViewHolder);
				break;

		}
		holder.mBinding.executePendingBindings();
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
			return ITEM_TYPE_WEB;
		}
		if (TextUtils.equals(entity.getReadableName(), "LANDMARK_DETECTION")) {
			return ITEM_TYPE_LANDMARK;
		}
		return super.getItemViewType(position);
	}


	static abstract class AbstractVisionViewHolder extends RecyclerView.ViewHolder {
		private final @NonNull ViewDataBinding mBinding;
		final @NonNull VisionEntityClickEvent mVisionEntityClickEvent = new VisionEntityClickEvent();
		private final @NonNull List<VisionEntity> mEntities;
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
	}

	public final static class WebViewHolder extends AbstractVisionViewHolder {
		private final @NonNull WebViewBinding mItemVisionWebBinding;

		private WebViewHolder(@NonNull WebViewBinding binding, @NonNull List<VisionEntity> entities, @NonNull VisionListAdapter adapter) {
			super(binding, entities, adapter);
			mItemVisionWebBinding = binding;
		}
	}

	public final static class LandmarkViewHolder extends AbstractVisionViewHolder {
		private final @NonNull LandmarkViewBinding mItemVisionLandmarkBinding;

		private LandmarkViewHolder(@NonNull LandmarkViewBinding binding, @NonNull List<VisionEntity> entries, @NonNull VisionListAdapter adapter) {
			super(binding, entries, adapter);
			mItemVisionLandmarkBinding = binding;
		}
	}


	public final static class WebCellViewHolder extends AbstractVisionViewHolder {
		private final @NonNull WebCellViewBinding mItemWebCellBinding;

		private WebCellViewHolder(@NonNull WebCellViewBinding binding, @NonNull List<VisionEntity> entities, @NonNull VisionListAdapter adapter) {
			super(binding, entities, adapter);
			mItemWebCellBinding = binding;
		}
	}

	public final static class LandmarkCellViewHolder extends AbstractVisionViewHolder {
		private final @NonNull LandmarkCellViewBinding mItemLandmarkCellBinding;

		private LandmarkCellViewHolder(@NonNull LandmarkCellViewBinding binding, @NonNull List<VisionEntity> entries, @NonNull VisionListAdapter adapter) {
			super(binding, entries, adapter);
			mItemLandmarkCellBinding = binding;
		}
	}
}
