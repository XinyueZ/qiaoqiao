package com.qiaoqiao.core.camera.awareness.ui

import android.widget.SeekBar

@android.databinding.BindingAdapter("bind:ajust")
fun setAdjustSeekBar(seekBar: SeekBar, adjust: Adjust) {
    if (adjust.intValue == 0) {
        adjust.onProgressChanged(seekBar, 0, false)
    }
    seekBar.setOnSeekBarChangeListener(adjust)
    seekBar.progress = adjust.intValue
}
