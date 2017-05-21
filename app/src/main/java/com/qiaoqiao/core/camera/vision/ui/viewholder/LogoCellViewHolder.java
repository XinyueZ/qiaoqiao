package com.qiaoqiao.core.camera.vision.ui.viewholder;

import android.support.annotation.NonNull;
import android.view.View;

import com.bumptech.glide.Glide;
import com.qiaoqiao.core.camera.vision.model.VisionEntity;
import com.qiaoqiao.databinding.LogoCellViewBinding;

import java.util.List;


public final class LogoCellViewHolder extends AbstractVisionViewHolder {
	private final @NonNull LogoCellViewBinding mItemLogoCellBinding;
	private final int mSize;

	public LogoCellViewHolder(@NonNull LogoCellViewBinding binding, @NonNull List<VisionEntity> entities, int size) {
		super(binding, entities);
		mItemLogoCellBinding = binding;
		mSize = size;
	}


	@Override
	public void onBindViewHolder() {
		VisionEntity entity = mEntities.get(getAdapterPosition());
		mItemLogoCellBinding.visionTv.setText(entity.getDescription()
		                                            .getDescriptionText());
		mItemLogoCellBinding.setVisionEntity(entity);
		mItemLogoCellBinding.setViewholder(this);

		mItemLogoCellBinding.visionIv.getLayoutParams().width = mSize;
		mItemLogoCellBinding.visionIv.getLayoutParams().height = mSize;

		loadImage(itemView.getContext(), entity, mItemLogoCellBinding.visionIv);
	}

	@Override
	public void onViewRecycled() {
		Glide.clear(mItemLogoCellBinding.visionIv);
	}

	@Override
	protected View getTransitionView() {
		return mItemLogoCellBinding.visionIv;
	}
}
