package com.qiaoqiao.core.camera.vision.ui

import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import com.qiaoqiao.R
import com.qiaoqiao.app.Key
import com.qiaoqiao.core.camera.vision.model.VisionEntity
import com.qiaoqiao.core.camera.vision.ui.viewholder.*
import com.qiaoqiao.databinding.LabelViewBinding
import com.qiaoqiao.databinding.LandmarkViewBinding
import com.qiaoqiao.databinding.LogoViewBinding
import com.qiaoqiao.databinding.WebViewBinding
import java.util.*

private const val ITEM_LAYOUT_WEB = R.layout.item_vision_web
private const val ITEM_LAYOUT_LANDMARK = R.layout.item_vision_landmark
private const val ITEM_LAYOUT_LOGO = R.layout.item_vision_logo
private const val ITEM_LAYOUT_LABEL = R.layout.item_vision_label
private const val ITEM_TYPE_WEB = 0x90
private const val ITEM_TYPE_LANDMARK = 0x91
private const val ITEM_TYPE_LOGO = 0x92
private const val ITEM_TYPE_LABEL = 0x93

class VisionListAdapter(private var key: Key, private val entities: LinkedList<VisionEntity> = LinkedList<VisionEntity>()) : RecyclerView.Adapter<AbstractVisionViewHolder>() {

    override fun getItemCount() = entities.size

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): AbstractVisionViewHolder {
        when (viewType) {
            ITEM_TYPE_WEB -> {
                val webBinding = DataBindingUtil.bind<WebViewBinding>(LayoutInflater.from(parent?.context)
                        .inflate(ITEM_LAYOUT_WEB, parent, false))
                return WebViewHolder(webBinding, entities)
            }
            ITEM_TYPE_LANDMARK -> {
                val landmarkBinding = DataBindingUtil.bind<LandmarkViewBinding>(LayoutInflater.from(parent?.context)
                        .inflate(ITEM_LAYOUT_LANDMARK, parent, false))
                return LandmarkViewHolder(landmarkBinding, entities, key)
            }
            ITEM_TYPE_LOGO -> {
                val logoBinding = DataBindingUtil.bind<LogoViewBinding>(LayoutInflater.from(parent?.context)
                        .inflate(ITEM_LAYOUT_LOGO, parent, false))
                return LogoViewHolder(logoBinding, entities)
            }
            else -> {
                // ITEM_TYPE_LABEL
                val labelBinding = DataBindingUtil.bind<LabelViewBinding>(LayoutInflater.from(parent?.context)
                        .inflate(ITEM_LAYOUT_LABEL, parent, false))
                return LabelViewHolder(labelBinding, entities)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        val entity = entities[position]
        if (TextUtils.equals(entity.readableName, "WEB_DETECTION")) {
            return ITEM_TYPE_WEB
        }
        if (TextUtils.equals(entity.readableName, "LOGO_DETECTION")) {
            return ITEM_TYPE_LOGO
        }
        if (TextUtils.equals(entity.readableName, "LABEL_DETECTION")) {
            return ITEM_TYPE_LABEL
        }
        if (TextUtils.equals(entity.readableName, "LANDMARK_DETECTION")) {
            return ITEM_TYPE_LANDMARK
        }
        return super.getItemViewType(position)
    }

    override fun onBindViewHolder(holder: AbstractVisionViewHolder?, position: Int) {
        holder?.onBindViewHolder()
        holder?.mBinding?.executePendingBindings()
    }

    override fun onViewRecycled(holder: AbstractVisionViewHolder?) {
        holder?.onViewRecycled()
    }

    fun addVisionEntityList(entityList: List<VisionEntity>) {
        for (e in entities) {
            e.isActivated = false
        }
        entities.addAll(0, entityList)
        notifyDataSetChanged()
    }

    fun clear() {
        entities.clear()
        notifyDataSetChanged()
    }
}