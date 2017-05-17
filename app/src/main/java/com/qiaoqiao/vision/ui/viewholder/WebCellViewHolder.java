package com.qiaoqiao.vision.ui.viewholder;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.qiaoqiao.R;
import com.qiaoqiao.databinding.WebCellViewBinding;
import com.qiaoqiao.vision.model.VisionEntity;

import java.util.List;
import java.util.Locale;


public final class WebCellViewHolder extends AbstractVisionViewHolder {
	private final @NonNull WebCellViewBinding mItemWebCellBinding;
	private final int mSize;

	public WebCellViewHolder(@NonNull WebCellViewBinding binding, @NonNull List<VisionEntity> entities, int size) {
		super(binding, entities);
		mItemWebCellBinding = binding;
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
		     .error(R.drawable.ic_default_image)
		     .crossFade()
		     .into(imageView);
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

	}

	@Override
	protected View getTransitionView() {
		return mItemWebCellBinding.visionIv;
	}
}
