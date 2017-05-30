package com.qiaoqiao.core.confidence.ui;


import android.support.annotation.NonNull;
import android.widget.SeekBar;

import com.amulyakhare.textdrawable.TextDrawable;

public final class BindingAdapter {
	@android.databinding.BindingAdapter({ "bind:textThumb" })
	public static void setTextDrawableOnSeekBar(@NonNull SeekBar seekBar, @NonNull TextDrawable textDrawable) {
		seekBar.setThumb(textDrawable);
	}
}
