package com.qiaoqiao.core.confidence.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.annotation.Px;
import android.support.v4.content.SharedPreferencesCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.preference.PreferenceManager;
import android.widget.SeekBar;

import com.amulyakhare.textdrawable.TextDrawable;
import com.qiaoqiao.R;

public final class Confidence implements SeekBar.OnSeekBarChangeListener {
	private @Px static final int W = 95;
	private @Px static final int H = 95;
	private float mValue;
	private String mKey;


	public Confidence(@NonNull String key, float value) {
		mValue = value;
		mKey = key;
	}

	public static Confidence createFromPrefs(@NonNull Context cxt, @NonNull String key) {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(cxt);
		return new Confidence(key, prefs.getFloat(key, 0.f));
	}

	public void save(@NonNull Context cxt) {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(cxt);
		final SharedPreferences.Editor edit = prefs.edit();
		edit.putFloat(mKey, mValue);
		SharedPreferencesCompat.EditorCompat.getInstance()
		                                    .apply(edit);
	}

	private TextDrawable createThumbDrawable(@NonNull Context cxt) {
		final Resources resources = cxt.getResources();
		return TextDrawable.builder()
		                   .beginConfig()
		                   .width(W)
		                   .height(H)
		                   .textColor(ResourcesCompat.getColor(resources, R.color.colorYellow, null))
		                   .endConfig()
		                   .buildRound(String.valueOf(mValue), ResourcesCompat.getColor(resources, R.color.colorPrimaryDark, null));
	}


	public float getValue() {
		return mValue;
	}

	int getIntValue() {
		return ((int) (getValue() * 100));
	}

	public String getKey() {
		return mKey;
	}


	@Override
	public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
		mValue = progress / 100.0f;
		seekBar.setThumb(createThumbDrawable(seekBar.getContext()));
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {

	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {

	}
}
