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
import com.qiaoqiao.bus.EntityClickEvent;
import com.qiaoqiao.databinding.ItemVisionLandmarkBinding;
import com.qiaoqiao.databinding.ItemVisionWebBinding;
import com.qiaoqiao.utils.LL;
import com.qiaoqiao.vision.model.VisionEntity;

import java.util.List;
import java.util.Stack;

public final class VisionListAdapter extends RecyclerView.Adapter<VisionListAdapter.AbstractVisionViewHolder> {
	private static final int ITEM_LAYOUT_WEB = R.layout.item_vision_web;
	private static final int ITEM_LAYOUT_LANDMARK = R.layout.item_vision_landmark;
	private static final int ITEM_TYPE_WEB = 0x91;
	private static final int ITEM_TYPE_LANDMARK = 0x92;
	private @NonNull final Stack<VisionEntity> mEntities = new Stack<>();

	@Override
	public AbstractVisionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		Context cxt = parent.getContext();
		switch (viewType) {
			case ITEM_TYPE_WEB:
				ItemVisionWebBinding webBinding = DataBindingUtil.bind(LayoutInflater.from(cxt)
				                                                                     .inflate(ITEM_LAYOUT_WEB, parent, false));
				return new WebViewHolder(webBinding, mEntities, this);
			case ITEM_TYPE_LANDMARK:
			default:
				ItemVisionLandmarkBinding landmarkBinding = DataBindingUtil.bind(LayoutInflater.from(cxt)
				                                                                               .inflate(ITEM_LAYOUT_LANDMARK, parent, false));
				return new LandmarkViewHolder(landmarkBinding, mEntities, this);
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
				break;
			case ITEM_TYPE_LANDMARK:
				LandmarkViewHolder landmarkViewHolder = (LandmarkViewHolder) holder;
				landmarkViewHolder.mItemVisionLandmarkBinding.visionTv.setText(entity.getDescription()
				                                                                     .getDescriptionText());
				break;

		}
		holder.mBinding.executePendingBindings();
	}

	public void addVisionEntity(@NonNull VisionEntity entity) {
		LL.d(entity.toString());
		mEntities.push(entity);
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
		private final @NonNull EntityClickEvent mEntityClickEvent = new EntityClickEvent();
		private final @NonNull List<VisionEntity> mEntities;
		private final @NonNull VisionListAdapter mVisionListAdapter;

		private AbstractVisionViewHolder(@NonNull ViewDataBinding binding, @NonNull List<VisionEntity> entities, @NonNull VisionListAdapter adapter) {
			super(binding.getRoot());
			mEntities = entities;
			mBinding = binding;
			mVisionListAdapter = adapter;
		}
	}

	private final static class WebViewHolder extends AbstractVisionViewHolder {
		private final @NonNull ItemVisionWebBinding mItemVisionWebBinding;

		private WebViewHolder(@NonNull ItemVisionWebBinding binding, @NonNull List<VisionEntity> entities, @NonNull VisionListAdapter adapter) {
			super(binding, entities, adapter);
			mItemVisionWebBinding = binding;
		}
	}

	private final static class LandmarkViewHolder extends AbstractVisionViewHolder {
		private final @NonNull ItemVisionLandmarkBinding mItemVisionLandmarkBinding;

		private LandmarkViewHolder(@NonNull ItemVisionLandmarkBinding binding, @NonNull List<VisionEntity> entries, @NonNull VisionListAdapter adapter) {
			super(binding, entries, adapter);
			mItemVisionLandmarkBinding = binding;
		}
	}
}
