package com.qiaoqiao.core.camera.vision.ui.viewholder;

import android.support.annotation.NonNull;
import android.view.View;

import com.bumptech.glide.Glide;
import com.qiaoqiao.core.camera.vision.model.VisionEntity;
import com.qiaoqiao.databinding.LogoViewBinding;

import java.util.List;

public final class LogoViewHolder extends AbstractVisionViewHolder {
	private final @NonNull LogoViewBinding mItemVisionLogoBinding;

	public LogoViewHolder(@NonNull LogoViewBinding binding, @NonNull List<VisionEntity> entities) {
		super(binding, entities);
		mItemVisionLogoBinding = binding;
	}

	@Override
	public void onBindViewHolder() {
		VisionEntity entity = mEntities.get(getAdapterPosition());
		mItemVisionLogoBinding.visionTv.setText(entity.getDescription()
		                                             .getDescriptionText());
		mItemVisionLogoBinding.setVisionEntity(entity);
		mItemVisionLogoBinding.setViewholder(this);
		mItemVisionLogoBinding.visionIv.setActivated(entity.isActivated());
		mItemVisionLogoBinding.visionTvFl.setActivated(entity.isActivated());
		loadImage(itemView.getContext(), entity, mItemVisionLogoBinding.visionIv);
	}

	@Override
	public void onViewRecycled() {
		Glide.clear(mItemVisionLogoBinding.visionIv);
	}

	@Override
	protected View getTransitionView() {
		return mItemVisionLogoBinding.visionIv;
	}
}
