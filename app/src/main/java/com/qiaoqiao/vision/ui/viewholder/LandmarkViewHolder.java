package com.qiaoqiao.vision.ui.viewholder;

import android.support.annotation.NonNull;
import android.view.View;

import com.qiaoqiao.databinding.LandmarkViewBinding;
import com.qiaoqiao.vision.model.VisionEntity;

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

		itemView.setActivated(entity.isActivated());
		itemView.setSelected(entity.isActivated());
	}

	@Override
	public void onViewRecycled() {

	}

	@Override
	protected View getTransitionView() {
		return mItemVisionLandmarkBinding.visionTv;
	}
}
