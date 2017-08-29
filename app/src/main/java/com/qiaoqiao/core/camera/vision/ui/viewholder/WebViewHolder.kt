package com.qiaoqiao.core.camera.vision.ui.viewholder

import android.view.View
import com.bumptech.glide.Glide
import com.qiaoqiao.core.camera.vision.model.VisionEntity
import com.qiaoqiao.databinding.WebViewBinding

internal class WebViewHolder(private val itemVisionWebBinding: WebViewBinding, entities: List<VisionEntity>) : AbstractVisionViewHolder(itemVisionWebBinding.root, entities) {
    override fun onBindViewHolder() {
        val entity = entities[adapterPosition]
        itemVisionWebBinding.visionTv.text = entity.description
                .descriptionText
        itemVisionWebBinding.visionEntity = entity
        itemVisionWebBinding.viewholder = this
        itemVisionWebBinding.visionIv.isActivated = entity.isActivated
        itemVisionWebBinding.visionTvFl.isActivated = entity.isActivated
        loadImage(itemView.context, entity, itemVisionWebBinding.visionIv)
    }

    override fun onViewRecycled() {
        Glide.with(itemVisionWebBinding.visionIv).clear(itemVisionWebBinding.visionIv)
    }

    override fun getTransitionView(): View {
        return itemVisionWebBinding.visionIv
    }

    override fun getBinding() = itemVisionWebBinding
}