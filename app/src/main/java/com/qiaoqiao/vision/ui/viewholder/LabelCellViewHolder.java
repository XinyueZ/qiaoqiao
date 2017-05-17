package com.qiaoqiao.vision.ui.viewholder;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.qiaoqiao.R;
import com.qiaoqiao.databinding.LabelCellViewBinding;
import com.qiaoqiao.vision.model.VisionEntity;

import java.util.List;
import java.util.Locale;


public final class LabelCellViewHolder extends AbstractVisionViewHolder {
	private final @NonNull LabelCellViewBinding mItemLabelCellBinding;
	private final int mSize;

	public LabelCellViewHolder(@NonNull LabelCellViewBinding binding, @NonNull List<VisionEntity> entities, int size) {
		super(binding, entities);
		mItemLabelCellBinding = binding;
		mSize = size;
	}


	private static void loadImage(Context cxt, VisionEntity entity, ImageView imageView) {
		String baseUrl = cxt.getString(R.string.base_url_knowledge);
		String imageUrl = String.format(baseUrl + "images/wikipedia?language=%s&keyword=%s",
		                                Locale.getDefault()
		                                      .getLanguage(),
		                                entity.getDescription()
		                                      .getDescriptionText());
		Glide.with(cxt)
		     .load(imageUrl)
		     .centerCrop()
		     .placeholder(R.drawable.ic_default_image)
		     .crossFade()
		     .into(imageView);
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
