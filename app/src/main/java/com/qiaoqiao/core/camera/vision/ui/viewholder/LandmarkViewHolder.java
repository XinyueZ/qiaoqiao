package com.qiaoqiao.core.camera.vision.ui.viewholder;

import android.support.annotation.NonNull;
import android.view.View;

import com.bumptech.glide.Glide;
import com.qiaoqiao.core.camera.vision.model.VisionEntity;
import com.qiaoqiao.databinding.LandmarkViewBinding;

import java.util.List;


public final class LandmarkViewHolder extends AbstractVisionViewHolder {
	private final @NonNull LandmarkViewBinding mItemVisionLandmarkBinding;

	public LandmarkViewHolder(@NonNull LandmarkViewBinding binding, @NonNull List<VisionEntity> entries) {
		super(binding, entries);
		mItemVisionLandmarkBinding = binding;
	}

	@Override
	public void onBindViewHolder() {
		VisionEntity entity = mEntities.get(getAdapterPosition());
		mItemVisionLandmarkBinding.visionTv.setText(entity.getDescription()
		                                                  .getDescriptionText());
		mItemVisionLandmarkBinding.setVisionEntity(entity);
		mItemVisionLandmarkBinding.setViewholder(this);

		mItemVisionLandmarkBinding.visionTvFl.setActivated(entity.isActivated());
		loadImage(itemView.getContext(), entity, mItemVisionLandmarkBinding.visionIv);
	}

	@Override
	public void onViewRecycled() {
		Glide.clear(mItemVisionLandmarkBinding.visionIv);
	}

	@Override
	protected View getTransitionView() {
		return mItemVisionLandmarkBinding.visionIv;
	}
}
