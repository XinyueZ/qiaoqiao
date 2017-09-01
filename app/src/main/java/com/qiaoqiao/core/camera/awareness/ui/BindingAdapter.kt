package com.qiaoqiao.core.camera.awareness.ui

import android.widget.ImageView
import android.widget.SeekBar
import com.qiaoqiao.core.camera.awareness.map.PlaceWrapper

@android.databinding.BindingAdapter("bind:adjust")
fun setAdjustSeekBar(seekBar: SeekBar, adjust: Adjust?) {
    if(adjust == null) return

    if (adjust.value == 0) {
        adjust.onProgressChanged(seekBar, 0, false)
    }

    seekBar.setOnSeekBarChangeListener(adjust)
    seekBar.progress = adjust.value
}

@android.databinding.BindingAdapter("bind:placeWrapper")
fun setPlaceWrapperImage(imageView: ImageView, placeWrapper: PlaceWrapper) = imageView.setImageBitmap(placeWrapper.bitmap)
