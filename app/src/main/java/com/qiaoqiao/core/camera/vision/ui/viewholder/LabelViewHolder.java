package com.qiaoqiao.core.camera.vision.ui.viewholder;

import android.support.annotation.NonNull;
import android.view.View;

import com.bumptech.glide.Glide;
import com.qiaoqiao.databinding.LabelViewBinding;
import com.qiaoqiao.core.camera.vision.model.VisionEntity;

import java.util.List;

public final class LabelViewHolder extends AbstractVisionViewHolder {
	private final @NonNull LabelViewBinding mItemVisionLabelBinding;

	public LabelViewHolder(@NonNull LabelViewBinding binding, @NonNull List<VisionEntity> entities) {
		super(binding, entities);
		mItemVisionLabelBinding = binding;
	}

	@Override
	public void onBindViewHolder() {
		VisionEntity entity = mEntities.get(getAdapterPosition());
		mItemVisionLabelBinding.visionTv.setText(entity.getDescription()
		                                             .getDescriptionText());
		mItemVisionLabelBinding.setVisionEntity(entity);
		mItemVisionLabelBinding.setViewholder(this);

		mItemVisionLabelBinding.visionTvFl.setActivated(entity.isActivated());
		loadImage(itemView.getContext(), entity, mItemVisionLabelBinding.visionIv);
	}

	@Override
	public void onViewRecycled() {
		Glide.clear(mItemVisionLabelBinding.visionIv);
	}

	@Override
	protected View getTransitionView() {
		return mItemVisionLabelBinding.visionIv;
	}
}
