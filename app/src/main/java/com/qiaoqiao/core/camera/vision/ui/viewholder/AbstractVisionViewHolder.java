package com.qiaoqiao.core.camera.vision.ui.viewholder;

import android.content.Context;
import android.databinding.ViewDataBinding;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.qiaoqiao.R;
import com.qiaoqiao.core.camera.vision.bus.VisionEntityClickEvent;
import com.qiaoqiao.core.camera.vision.model.VisionEntity;

import java.util.List;
import java.util.Locale;

import de.greenrobot.event.EventBus;


public abstract class AbstractVisionViewHolder extends RecyclerView.ViewHolder {
	public final @NonNull ViewDataBinding mBinding;
	private final @NonNull VisionEntityClickEvent mVisionEntityClickEvent = new VisionEntityClickEvent();
	final @NonNull List<VisionEntity> mEntities;

	AbstractVisionViewHolder(@NonNull ViewDataBinding binding, @NonNull List<VisionEntity> entities) {
		super(binding.getRoot());
		mEntities = entities;
		mBinding = binding;
	}

	protected static void loadImage(Context cxt, VisionEntity entity, ImageView imageView) {
		String baseUrl = cxt.getString(R.string.base_url_knowledge);
		String imageUrl = String.format(baseUrl + "images/wikipedia?language=%s&keyword=%s",
		                                Locale.getDefault()
		                                      .getLanguage(),
		                                entity.getDescription()
		                                      .getDescriptionText());
		Glide.with(cxt)
		     .load(imageUrl)
		     .crossFade()
		     .centerCrop()
		     .diskCacheStrategy(DiskCacheStrategy.ALL)
		     .skipMemoryCache(false)
		     .placeholder(R.drawable.ic_default_image)
		     .error(R.drawable.ic_default_image)
		     .crossFade()
		     .into(imageView);
	}

	public void onClicked(VisionEntity visionEntity) {
		mVisionEntityClickEvent.setEntity(visionEntity);


		ViewCompat.setTransitionName(getTransitionView(),
		                             itemView.getContext()
		                                     .getString(R.string.transition_share_item_name) + "-" + visionEntity.getId() + "-" + visionEntity.getDescription());
		mVisionEntityClickEvent.setTransitionView(getTransitionView());
		EventBus.getDefault()
		        .post(mVisionEntityClickEvent);
	}

	protected abstract View getTransitionView();

	public abstract void onBindViewHolder();

	public abstract void onViewRecycled();
}
