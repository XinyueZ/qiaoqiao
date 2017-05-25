package com.qiaoqiao.core.camera.vision.ui.viewholder;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.maps.model.LatLng;
import com.qiaoqiao.R;
import com.qiaoqiao.app.Key;
import com.qiaoqiao.core.camera.vision.model.VisionEntity;
import com.qiaoqiao.databinding.LandmarkViewBinding;

import java.util.List;


public final class LandmarkViewHolder extends AbstractVisionViewHolder {
	private final @NonNull LandmarkViewBinding mItemVisionLandmarkBinding;
	private final @NonNull Key mKey;

	public LandmarkViewHolder(@NonNull LandmarkViewBinding binding, @NonNull List<VisionEntity> entries,  @NonNull Key key) {
		super(binding, entries);
		mItemVisionLandmarkBinding = binding;
		mKey = key;
	}

	@Override
	protected void loadImage(Context cxt, VisionEntity entity, ImageView imageView) {
		final LatLng latLng = entity.getLocation()
		                            .toLatLng();
		String latlng  = latLng.latitude + "," + latLng.longitude;
		String url = "https://maps.googleapis.com/maps/api/staticmap?center=" + latlng +
				"&zoom=15&size=180x180&markers=color:red%7Clabel:S%7C" + latlng + "&key=" +
				mKey.toString() + "&sensor=true&maptype=roadmap" ;

		Glide.with(cxt)
		     .load(url)
		     .dontAnimate()
		     .centerCrop()
		     .diskCacheStrategy(DiskCacheStrategy.ALL)
		     .skipMemoryCache(false)
		     .placeholder(R.drawable.ic_default_image)
		     .error(R.drawable.ic_default_image)
		     .crossFade()
		     .into(imageView);
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
