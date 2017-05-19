package com.qiaoqiao.core.camera.vision.ui.viewholder;

import android.support.annotation.NonNull;
import android.view.View;

import com.qiaoqiao.databinding.LabelCellViewBinding;
import com.qiaoqiao.core.camera.vision.model.VisionEntity;

import java.util.List;


public final class LabelCellViewHolder extends AbstractVisionViewHolder {
	private final @NonNull LabelCellViewBinding mItemLabelCellBinding;
	private final int mSize;

	public LabelCellViewHolder(@NonNull LabelCellViewBinding binding, @NonNull List<VisionEntity> entities, int size) {
		super(binding, entities);
		mItemLabelCellBinding = binding;
		mSize = size;
	}


	@Override
	public void onBindViewHolder() {
		VisionEntity entity = mEntities.get(getAdapterPosition());
		mItemLabelCellBinding.visionTv.setText(entity.getDescription()
		                                             .getDescriptionText());
		mItemLabelCellBinding.setVisionEntity(entity);
		mItemLabelCellBinding.setViewholder(this);

		mItemLabelCellBinding.visionIv.getLayoutParams().width = mSize;
		mItemLabelCellBinding.visionIv.getLayoutParams().height = mSize;

		loadImage(itemView.getContext(), entity, mItemLabelCellBinding.visionIv);
	}

	@Override
	public void onViewRecycled() {

	}

	@Override
	protected View getTransitionView() {
		return mItemLabelCellBinding.visionIv;
	}
}
