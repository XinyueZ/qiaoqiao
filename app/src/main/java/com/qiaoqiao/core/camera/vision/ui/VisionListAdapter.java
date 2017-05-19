package com.qiaoqiao.core.camera.vision.ui;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.qiaoqiao.R;
import com.qiaoqiao.databinding.LabelCellViewBinding;
import com.qiaoqiao.databinding.LabelViewBinding;
import com.qiaoqiao.databinding.LandmarkCellViewBinding;
import com.qiaoqiao.databinding.LandmarkViewBinding;
import com.qiaoqiao.databinding.LogoCellViewBinding;
import com.qiaoqiao.databinding.LogoViewBinding;
import com.qiaoqiao.databinding.WebCellViewBinding;
import com.qiaoqiao.databinding.WebViewBinding;
import com.qiaoqiao.utils.DeviceUtils;
import com.qiaoqiao.core.camera.vision.model.VisionEntity;
import com.qiaoqiao.core.camera.vision.ui.viewholder.AbstractVisionViewHolder;
import com.qiaoqiao.core.camera.vision.ui.viewholder.LabelCellViewHolder;
import com.qiaoqiao.core.camera.vision.ui.viewholder.LabelViewHolder;
import com.qiaoqiao.core.camera.vision.ui.viewholder.LandmarkCellViewHolder;
import com.qiaoqiao.core.camera.vision.ui.viewholder.LandmarkViewHolder;
import com.qiaoqiao.core.camera.vision.ui.viewholder.LogoCellViewHolder;
import com.qiaoqiao.core.camera.vision.ui.viewholder.LogoViewHolder;
import com.qiaoqiao.core.camera.vision.ui.viewholder.WebCellViewHolder;
import com.qiaoqiao.core.camera.vision.ui.viewholder.WebViewHolder;

import java.util.LinkedList;
import java.util.List;

public final class VisionListAdapter extends RecyclerView.Adapter<AbstractVisionViewHolder> {
	private static final int ITEM_LAYOUT_WEB = R.layout.item_vision_web;
	private static final int ITEM_LAYOUT_LANDMARK = R.layout.item_vision_landmark;
	private static final int ITEM_LAYOUT_LOGO = R.layout.item_vision_logo;
	private static final int ITEM_LAYOUT_LABEL = R.layout.item_vision_label;
	private static final int ITEM_LAYOUT_WEB_CELL = R.layout.item_vision_web_cell;
	private static final int ITEM_LAYOUT_LANDMARK_CELL = R.layout.item_vision_landmark_cell;
	private static final int ITEM_LAYOUT_LOGO_CELL = R.layout.item_vision_logo_cell;
	private static final int ITEM_LAYOUT_LABEL_CELL = R.layout.item_vision_label_cell;
	private static final int ITEM_TYPE_WEB = 0x90;
	private static final int ITEM_TYPE_LANDMARK = 0x91;
	private static final int ITEM_TYPE_LOGO = 0x92;
	private static final int ITEM_TYPE_LABEL = 0x93;
	private static final int ITEM_TYPE_WEB_CELL = 0x94;
	private static final int ITEM_TYPE_LANDMARK_CELL = 0x95;
	private static final int ITEM_TYPE_LOGO_CELL = 0x96;
	private static final int ITEM_TYPE_LABEL_CELL = 0x97;

	private int mColumns;
	private @NonNull final List<VisionEntity> mEntities = new LinkedList<>();

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
				return new WebViewHolder(webBinding, mEntities);
			case ITEM_TYPE_LANDMARK:
				LandmarkViewBinding landmarkBinding = DataBindingUtil.bind(LayoutInflater.from(cxt)
				                                                                         .inflate(ITEM_LAYOUT_LANDMARK, parent, false));
				return new LandmarkViewHolder(landmarkBinding, mEntities);
			case ITEM_TYPE_LOGO:
				LogoViewBinding logoBinding = DataBindingUtil.bind(LayoutInflater.from(cxt)
				                                                                 .inflate(ITEM_LAYOUT_LOGO, parent, false));
				return new LogoViewHolder(logoBinding, mEntities);
			case ITEM_TYPE_LABEL:
				LabelViewBinding labelBinding = DataBindingUtil.bind(LayoutInflater.from(cxt)
				                                                                   .inflate(ITEM_LAYOUT_LABEL, parent, false));
				return new LabelViewHolder(labelBinding, mEntities);
			case ITEM_TYPE_WEB_CELL:
				WebCellViewBinding webCellBinding = DataBindingUtil.bind(LayoutInflater.from(cxt)
				                                                                       .inflate(ITEM_LAYOUT_WEB_CELL, parent, false));
				return new WebCellViewHolder(webCellBinding, mEntities, size);
			case ITEM_TYPE_LANDMARK_CELL:

				LandmarkCellViewBinding landmarkCellBinding = DataBindingUtil.bind(LayoutInflater.from(cxt)
				                                                                                 .inflate(ITEM_LAYOUT_LANDMARK_CELL, parent, false));
				return new LandmarkCellViewHolder(landmarkCellBinding, mEntities, size);
			case ITEM_TYPE_LOGO_CELL:
				LogoCellViewBinding logoCellBinding = DataBindingUtil.bind(LayoutInflater.from(cxt)
				                                                                         .inflate(ITEM_LAYOUT_LOGO_CELL, parent, false));
				return new LogoCellViewHolder(logoCellBinding, mEntities, size);
			case ITEM_TYPE_LABEL_CELL:
			default:
				LabelCellViewBinding labelCellBinding = DataBindingUtil.bind(LayoutInflater.from(cxt)
				                                                                           .inflate(ITEM_LAYOUT_LABEL_CELL, parent, false));
				return new LabelCellViewHolder(labelCellBinding, mEntities, size);
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


	void addVisionEntityList(@NonNull List<VisionEntity> entityList) {
		for (VisionEntity e : mEntities) {
			e.setActivated(false);
		}
		mEntities.addAll(0, entityList);
		notifyDataSetChanged();
	}

	@Override
	public int getItemCount() {
		return mEntities.size();
	}

	@Override
	public int getItemViewType(int position) {
		VisionEntity entity = mEntities.get(position);
		if (TextUtils.equals(entity.getReadableName(), "WEB_DETECTION") ) {
			return entity.isInCell() ?
			       ITEM_TYPE_WEB_CELL :
			       ITEM_TYPE_WEB;
		}
		if (TextUtils.equals(entity.getReadableName(), "LOGO_DETECTION")) {
			return entity.isInCell() ?
			       ITEM_TYPE_LOGO_CELL :
			       ITEM_TYPE_LOGO;
		}
		if (TextUtils.equals(entity.getReadableName(), "LABEL_DETECTION")) {
			return entity.isInCell() ?
			       ITEM_TYPE_LABEL_CELL :
			       ITEM_TYPE_LABEL;
		}
		if (TextUtils.equals(entity.getReadableName(), "LANDMARK_DETECTION")) {
			return entity.isInCell() ?
			       ITEM_TYPE_LANDMARK_CELL :
			       ITEM_TYPE_LANDMARK;
		}
		return super.getItemViewType(position);
	}


}
