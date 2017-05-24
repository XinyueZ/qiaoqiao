package com.qiaoqiao.core.camera.vision.ui.viewholder;

import android.support.annotation.NonNull;
import android.view.View;

import com.qiaoqiao.databinding.WebViewBinding;
import com.qiaoqiao.core.camera.vision.model.VisionEntity;

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

		itemView.setActivated(entity.isActivated());
	}

	@Override
	public void onViewRecycled() {

	}

	@Override
	protected View getTransitionView() {
		return mItemVisionWebBinding.visionTv;
	}
}
