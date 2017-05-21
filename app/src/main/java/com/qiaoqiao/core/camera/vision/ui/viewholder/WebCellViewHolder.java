package com.qiaoqiao.core.camera.vision.ui.viewholder;

import android.support.annotation.NonNull;
import android.view.View;

import com.bumptech.glide.Glide;
import com.qiaoqiao.core.camera.vision.model.VisionEntity;
import com.qiaoqiao.databinding.WebCellViewBinding;

import java.util.List;


public final class WebCellViewHolder extends AbstractVisionViewHolder {
	private final @NonNull WebCellViewBinding mItemWebCellBinding;
	private final int mSize;

	public WebCellViewHolder(@NonNull WebCellViewBinding binding, @NonNull List<VisionEntity> entities, int size) {
		super(binding, entities);
		mItemWebCellBinding = binding;
		mSize = size;
	}


	@Override
	public void onBindViewHolder() {
		VisionEntity entity = mEntities.get(getAdapterPosition());
		mItemWebCellBinding.visionTv.setText(entity.getDescription()
		                                           .getDescriptionText());
		mItemWebCellBinding.setVisionEntity(entity);
		mItemWebCellBinding.setViewholder(this);

		mItemWebCellBinding.visionIv.getLayoutParams().width = mSize;
		mItemWebCellBinding.visionIv.getLayoutParams().height = mSize;

		loadImage(itemView.getContext(), entity, mItemWebCellBinding.visionIv);
	}

	@Override
	public void onViewRecycled() {
		Glide.clear(mItemWebCellBinding.visionIv);
	}

	@Override
	protected View getTransitionView() {
		return mItemWebCellBinding.visionIv;
	}
}
