package com.qiaoqiao.core.confidence.ui;


import android.support.annotation.NonNull;
import android.widget.SeekBar;

public final class BindingAdapter {

	@android.databinding.BindingAdapter({ "bind:confidence" })
	public static void setSeekBar(@NonNull SeekBar seekBar, @NonNull Confidence confidence) {
		if (confidence.getIntValue() == 0) {
			confidence.onProgressChanged(seekBar, 0, false);
		}
		seekBar.setOnSeekBarChangeListener(confidence);
		seekBar.setProgress(confidence.getIntValue());
	}
}
