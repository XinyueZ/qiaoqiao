package com.qiaoqiao.core.camera.vision.ui;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.qiaoqiao.R;
import com.qiaoqiao.app.Key;
import com.qiaoqiao.core.camera.vision.model.VisionEntity;
import com.qiaoqiao.core.camera.vision.ui.viewholder.AbstractVisionViewHolder;
import com.qiaoqiao.core.camera.vision.ui.viewholder.LabelViewHolder;
import com.qiaoqiao.core.camera.vision.ui.viewholder.LandmarkViewHolder;
import com.qiaoqiao.core.camera.vision.ui.viewholder.LogoViewHolder;
import com.qiaoqiao.core.camera.vision.ui.viewholder.WebViewHolder;
import com.qiaoqiao.databinding.LabelViewBinding;
import com.qiaoqiao.databinding.LandmarkViewBinding;
import com.qiaoqiao.databinding.LogoViewBinding;
import com.qiaoqiao.databinding.WebViewBinding;

import java.util.LinkedList;
import java.util.List;

public final class VisionListAdapter extends RecyclerView.Adapter<AbstractVisionViewHolder> {
	private static final int ITEM_LAYOUT_WEB = R.layout.item_vision_web;
	private static final int ITEM_LAYOUT_LANDMARK = R.layout.item_vision_landmark;
	private static final int ITEM_LAYOUT_LOGO = R.layout.item_vision_logo;
	private static final int ITEM_LAYOUT_LABEL = R.layout.item_vision_label;
	private static final int ITEM_TYPE_WEB = 0x90;
	private static final int ITEM_TYPE_LANDMARK = 0x91;
	private static final int ITEM_TYPE_LOGO = 0x92;
	private static final int ITEM_TYPE_LABEL = 0x93;
	private @NonNull Key mKey;
	private @NonNull final List<VisionEntity> mEntities = new LinkedList<>();


	VisionListAdapter(@NonNull Key key) {
		mKey = key;
	}

	@Override
	public AbstractVisionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		Context cxt = parent.getContext();

		switch (viewType) {
			case ITEM_TYPE_WEB:
				WebViewBinding webBinding = DataBindingUtil.bind(LayoutInflater.from(cxt)
				                                                               .inflate(ITEM_LAYOUT_WEB, parent, false));
				return new WebViewHolder(webBinding, mEntities);
			case ITEM_TYPE_LANDMARK:
				LandmarkViewBinding landmarkBinding = DataBindingUtil.bind(LayoutInflater.from(cxt)
				                                                                         .inflate(ITEM_LAYOUT_LANDMARK, parent, false));
				return new LandmarkViewHolder(landmarkBinding, mEntities, mKey);
			case ITEM_TYPE_LOGO:
				LogoViewBinding logoBinding = DataBindingUtil.bind(LayoutInflater.from(cxt)
				                                                                 .inflate(ITEM_LAYOUT_LOGO, parent, false));
				return new LogoViewHolder(logoBinding, mEntities);
			case ITEM_TYPE_LABEL:
			default:
				LabelViewBinding labelBinding = DataBindingUtil.bind(LayoutInflater.from(cxt)
				                                                                   .inflate(ITEM_LAYOUT_LABEL, parent, false));
				return new LabelViewHolder(labelBinding, mEntities);


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
		if (TextUtils.equals(entity.getReadableName(), "WEB_DETECTION")) {
			return ITEM_TYPE_WEB;
		}
		if (TextUtils.equals(entity.getReadableName(), "LOGO_DETECTION")) {
			return ITEM_TYPE_LOGO;
		}
		if (TextUtils.equals(entity.getReadableName(), "LABEL_DETECTION")) {
			return ITEM_TYPE_LABEL;
		}
		if (TextUtils.equals(entity.getReadableName(), "LANDMARK_DETECTION")) {
			return ITEM_TYPE_LANDMARK;
		}
		return super.getItemViewType(position);
	}


}
