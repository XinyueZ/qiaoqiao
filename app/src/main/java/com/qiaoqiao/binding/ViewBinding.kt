package com.qiaoqiao.binding

import android.databinding.BindingAdapter
import android.view.View
import android.widget.SeekBar

@BindingAdapter("clickListener")
fun View.setOnClick(clickListener: View.OnClickListener) {
    setOnClickListener(clickListener)
    setClickable(true)
}

@BindingAdapter("progressChanged")
fun SeekBar.setOnProgressChanged(onSeekBarChangeListener: SeekBar.OnSeekBarChangeListener) {
    setOnSeekBarChangeListener(onSeekBarChangeListener)
}