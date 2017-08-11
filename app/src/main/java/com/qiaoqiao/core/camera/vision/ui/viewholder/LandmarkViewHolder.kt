package com.qiaoqiao.core.camera.vision.ui.viewholder

import android.content.Context
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.qiaoqiao.R
import com.qiaoqiao.app.Key
import com.qiaoqiao.core.camera.vision.model.VisionEntity
import com.qiaoqiao.databinding.LandmarkViewBinding

internal class LandmarkViewHolder(private val itemVisionLandmarkBinding: LandmarkViewBinding, entities: List<VisionEntity>, private val key: Key) : AbstractVisionViewHolder(itemVisionLandmarkBinding.root, entities) {

    override fun loadImage(cxt: Context, entity: VisionEntity, imageView: ImageView) {
        val latLng = entity.location
                .toLatLng()
        val latlng = latLng!!.latitude.toString() + "," + latLng.longitude
        val url = "https://maps.googleapis.com/maps/api/staticmap?center=" + latlng +
                "&zoom=15&size=180x180&markers=color:red%7Clabel:S%7C" + latlng + "&key=" +
                key.toString() + "&sensor=true&maptype=roadmap"

        Glide.with(cxt)
                .load(url)
                .dontAnimate()
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .skipMemoryCache(false)
                .placeholder(R.drawable.ic_default_image)
                .error(R.drawable.ic_default_image)
                .crossFade()
                .into(imageView)
    }

    override fun onBindViewHolder() {
        val entity = entities[adapterPosition]
        itemVisionLandmarkBinding.visionTv.text = entity.description
                .descriptionText
        itemVisionLandmarkBinding.visionEntity = entity
        itemVisionLandmarkBinding.viewholder = this
        itemVisionLandmarkBinding.visionIv.isActivated = entity.isActivated
        itemVisionLandmarkBinding.visionTvFl.isActivated = entity.isActivated
        loadImage(itemView.context, entity, itemVisionLandmarkBinding.visionIv)
    }

    override fun onViewRecycled() {
        Glide.clear(itemVisionLandmarkBinding.visionIv)
    }

    override fun getTransitionView(): View {
        return itemVisionLandmarkBinding.visionIv
    }

    override fun getBinding() = itemVisionLandmarkBinding
}