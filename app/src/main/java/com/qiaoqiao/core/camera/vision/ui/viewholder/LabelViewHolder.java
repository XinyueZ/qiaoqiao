package com.qiaoqiao.core.camera.vision.ui.viewholder;

import android.support.annotation.NonNull;
import android.view.View;

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

		itemView.setSelected(entity.isActivated());
	}

	@Override
	public void onViewRecycled() {

	}

	@Override
	protected View getTransitionView() {
		return mItemVisionLabelBinding.visionTv;
	}
}
