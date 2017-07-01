package com.qiaoqiao.core.camera.vision.ui.viewholder

import android.view.View
import com.bumptech.glide.Glide
import com.qiaoqiao.core.camera.vision.model.VisionEntity
import com.qiaoqiao.databinding.LogoViewBinding

class LogoViewHolder(private val itemVisionLogoBinding: LogoViewBinding, entities: List<VisionEntity>) : AbstractVisionViewHolder(itemVisionLogoBinding.root, entities) {
    override fun onBindViewHolder() {
        val entity = entities[adapterPosition]
        itemVisionLogoBinding.visionTv.text = entity.description
                .descriptionText
        itemVisionLogoBinding.visionEntity = entity
        itemVisionLogoBinding.viewholder = this
        itemVisionLogoBinding.visionIv.isActivated = entity.isActivated
        itemVisionLogoBinding.visionTvFl.isActivated = entity.isActivated
        loadImage(itemView.context, entity, itemVisionLogoBinding.visionIv)
    }

    override fun onViewRecycled() {
        Glide.clear(itemVisionLogoBinding.visionIv)
    }

    override fun getTransitionView(): View {
        return itemVisionLogoBinding.visionIv
    }

    override fun getBinding() = itemVisionLogoBinding
}