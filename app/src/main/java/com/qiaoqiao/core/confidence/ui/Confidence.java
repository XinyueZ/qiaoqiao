package com.qiaoqiao.core.confidence.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.v4.content.SharedPreferencesCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.preference.PreferenceManager;
import android.widget.SeekBar;

import com.amulyakhare.textdrawable.TextDrawable;
import com.qiaoqiao.R;

public final class Confidence implements SeekBar.OnSeekBarChangeListener {
	private float mValue;
	private final String mKey;
	private final int mThumbWidth;
	private final int mThumbHeight;
	private final int mTextColor;
	private final int mThumbColor;

	private Confidence(@NonNull Context cxt, @NonNull String key, float value) {
		mValue = value;
		mKey = key;
		Resources resources = cxt.getResources();
		mThumbWidth = resources.getDimensionPixelSize(R.dimen.seek_bar_thumb_size);
		mThumbHeight = resources.getDimensionPixelSize(R.dimen.seek_bar_thumb_size);
		mTextColor = ResourcesCompat.getColor(resources, R.color.colorYellow, null);
		mThumbColor = ResourcesCompat.getColor(resources, R.color.colorPrimaryDark, null);
	}

	public @NonNull
	Confidence setValue(float value) {
		mValue = value;
		return this;
	}

	public static Confidence createFromPrefs(@NonNull Context cxt, @NonNull String key, float defaultValue) {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(cxt);
		return new Confidence(cxt, key, prefs.getFloat(key, defaultValue));
	}

	public static float getValueOnly(@NonNull Context cxt, @NonNull String key, float defaultValue) {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(cxt);
		return prefs.getFloat(key, defaultValue);
	}

	public void save(@NonNull Context cxt) {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(cxt);
		final SharedPreferences.Editor edit = prefs.edit();
		edit.putFloat(mKey, mValue);
		SharedPreferencesCompat.EditorCompat.getInstance()
		                                    .apply(edit);
	}

	private TextDrawable createThumbDrawable() {
		return TextDrawable.builder()
		                   .beginConfig()
		                   .width(mThumbWidth)
		                   .height(mThumbHeight)
		                   .textColor(mTextColor)
		                   .endConfig()
		                   .buildRound(String.valueOf(mValue), mThumbColor);
	}


	public float getValue() {
		return mValue;
	}

	int getIntValue() {
		return ((int) (getValue() * 100));
	}


	@Override
	public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
		mValue = progress / 100.0f;
		seekBar.setThumb(createThumbDrawable());
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {

	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {

	}
}
