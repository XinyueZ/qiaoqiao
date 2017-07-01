package com.qiaoqiao.core.camera.vision.ui.viewholder

import android.content.Context
import android.databinding.ViewDataBinding
import android.support.v4.view.ViewCompat
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.qiaoqiao.R
import com.qiaoqiao.core.camera.vision.bus.VisionEntityClickEvent
import com.qiaoqiao.core.camera.vision.model.VisionEntity
import de.greenrobot.event.EventBus
import java.util.*

abstract class AbstractVisionViewHolder(rootView: View, protected val entities: List<VisionEntity>) : RecyclerView.ViewHolder(rootView) {
    private val visionEntityClickEvent: VisionEntityClickEvent = VisionEntityClickEvent()

    protected open fun loadImage(cxt: Context, entity: VisionEntity, imageView: ImageView) {
        val baseUrl = cxt.getString(R.string.base_url_knowledge)
        val imageUrl = String.format(baseUrl + "thumbnails/wikipedia?language=%s&keyword=%s",
                Locale.getDefault()
                        .language,
                entity.description
                        .descriptionText)
        Glide.with(cxt)
                .load(imageUrl)
                .dontAnimate()
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .skipMemoryCache(false)
                .placeholder(R.drawable.ic_default_image)
                .error(R.drawable.ic_default_image)
                .crossFade()
                .into(imageView)
    }

    fun onClicked(visionEntity: VisionEntity) {
        visionEntityClickEvent.entity = visionEntity
        ViewCompat.setTransitionName(getTransitionView(),
                itemView.context
                        .getString(R.string.transition_share_item_name) + "-" + visionEntity.id + "-" + visionEntity.description)
        visionEntityClickEvent.transitionView = getTransitionView()
        EventBus.getDefault()
                .post(visionEntityClickEvent)
    }

    protected abstract fun getTransitionView(): View

    abstract fun onBindViewHolder()

    abstract fun onViewRecycled()

    abstract fun getBinding(): ViewDataBinding
}