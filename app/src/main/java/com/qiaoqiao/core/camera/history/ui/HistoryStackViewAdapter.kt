package com.qiaoqiao.core.camera.history.ui

import android.databinding.DataBindingUtil
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.qiaoqiao.R
import com.qiaoqiao.app.GlideApp
import com.qiaoqiao.core.camera.history.bus.HistoryItemClickEvent
import com.qiaoqiao.databinding.ItemStackviewHistoryBinding
import com.qiaoqiao.repository.database.HistoryItem
import de.greenrobot.event.EventBus

private val ITEM_LAYOUT = R.layout.item_stackview_history

class HistoryStackViewAdapter(val list: MutableList<HistoryItem>, val layoutInflater: LayoutInflater) : BaseAdapter() {
    override fun getItem(position: Int): HistoryItem? = list[position]
    override fun getItemId(position: Int) = position.toLong()
    override fun getCount() = list.size

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
        var cv: View? = convertView
        val holder: ViewHolder
        val historyItem = list[position]
        if (cv == null) {
            holder = ViewHolder(DataBindingUtil.inflate(layoutInflater, ITEM_LAYOUT, parent, false)!!)
            cv = holder.binding.root
            holder.binding.root.tag = holder
        } else {
            holder = cv.tag as ViewHolder
        }
        val cxt = holder.binding.root.context
        if (TextUtils.isEmpty(historyItem.imageUri)) {
            holder.binding.historyItemIv.setImageResource(R.drawable.ic_default_image)
        } else {
            try {
                GlideApp.with(cxt)
                        .load(historyItem.imageUri)
                        .placeholder(R.drawable.ic_default_image)
                        .into(holder.binding.historyItemIv)
            } catch (e: IllegalArgumentException) {
            }
        }
        holder.binding.viewholder = holder
        holder.binding.historyItem = historyItem
        holder.binding.executePendingBindings()
        return cv
    }

    class ViewHolder(val binding: ItemStackviewHistoryBinding, val historyItemClickEvent: HistoryItemClickEvent = HistoryItemClickEvent(null)) {
        fun onEntryClicked(historyItem: HistoryItem) {
            historyItemClickEvent.historyItem = historyItem
            EventBus.getDefault()
                    .post(historyItemClickEvent)
        }
    }
}