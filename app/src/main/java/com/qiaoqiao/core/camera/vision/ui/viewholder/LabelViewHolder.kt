package com.qiaoqiao.core.camera.vision.ui.viewholder

import android.view.View
import com.bumptech.glide.Glide
import com.qiaoqiao.core.camera.vision.model.VisionEntity
import com.qiaoqiao.databinding.LabelViewBinding

class LabelViewHolder(private val itemVisionLabelBinding: LabelViewBinding, entities: List<VisionEntity>) : AbstractVisionViewHolder(itemVisionLabelBinding.root, entities) {

    override fun onBindViewHolder() {
        val entity = entities[adapterPosition]
        itemVisionLabelBinding.visionTv.text = entity.description
                .descriptionText
        itemVisionLabelBinding.visionEntity = entity
        itemVisionLabelBinding.viewholder = this
        itemVisionLabelBinding.visionIv.isActivated = entity.isActivated
        itemVisionLabelBinding.visionTvFl.isActivated = entity.isActivated
        loadImage(itemView.context, entity, itemVisionLabelBinding.visionIv)
    }

    override fun onViewRecycled() {
        Glide.clear(itemVisionLabelBinding.visionIv)
    }

    override fun getTransitionView(): View {
        return itemVisionLabelBinding.visionIv
    }

    override fun getBinding() = itemVisionLabelBinding
}