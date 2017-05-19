package com.qiaoqiao.core.camera.vision.ui.viewholder;

import android.support.annotation.NonNull;
import android.view.View;

import com.qiaoqiao.databinding.LogoViewBinding;
import com.qiaoqiao.core.camera.vision.model.VisionEntity;

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

		itemView.setActivated(entity.isActivated());
		itemView.setSelected(entity.isActivated());
	}

	@Override
	public void onViewRecycled() {

	}

	@Override
	protected View getTransitionView() {
		return mItemVisionLogoBinding.visionTv;
	}
}
