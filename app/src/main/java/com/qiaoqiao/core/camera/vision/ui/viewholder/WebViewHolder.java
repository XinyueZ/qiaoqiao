package com.qiaoqiao.core.camera.vision.ui.viewholder;

import android.support.annotation.NonNull;
import android.view.View;

import com.bumptech.glide.Glide;
import com.qiaoqiao.core.camera.vision.model.VisionEntity;
import com.qiaoqiao.databinding.WebViewBinding;

import java.util.List;

public final class WebViewHolder extends AbstractVisionViewHolder {
	private final @NonNull WebViewBinding mItemVisionWebBinding;

	public WebViewHolder(@NonNull WebViewBinding binding, @NonNull List<VisionEntity> entities) {
		super(binding, entities);
		mItemVisionWebBinding = binding;
	}

	@Override
	public void onBindViewHolder() {
		VisionEntity entity = mEntities.get(getAdapterPosition());
		mItemVisionWebBinding.visionTv.setText(entity.getDescription()
		                                             .getDescriptionText());
		mItemVisionWebBinding.setVisionEntity(entity);
		mItemVisionWebBinding.setViewholder(this);
		mItemVisionWebBinding.visionIv.setActivated(entity.isActivated());
		mItemVisionWebBinding.visionTvFl.setActivated(entity.isActivated());
		loadImage(itemView.getContext(), entity, mItemVisionWebBinding.visionIv);
	}

	@Override
	public void onViewRecycled() {
		Glide.clear(mItemVisionWebBinding.visionIv);
	}

	@Override
	protected View getTransitionView() {
		return mItemVisionWebBinding.visionIv;
	}
}
