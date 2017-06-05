package com.qiaoqiao.core.camera.awareness.ui

import android.content.Context
import android.support.annotation.NonNull
import android.support.v4.content.SharedPreferencesCompat
import android.support.v4.content.res.ResourcesCompat
import android.support.v7.preference.PreferenceManager
import android.widget.SeekBar
import com.amulyakhare.textdrawable.TextDrawable
import com.qiaoqiao.R

class Adjust(@NonNull val cxt: Context, @NonNull val key: String, var value: Float) :
        SeekBar.OnSeekBarChangeListener {
    private var thumbWidth: Int = cxt.resources.getDimensionPixelSize(R.dimen.seek_bar_thumb_size)

    private var thumbHeight: Int = cxt.resources.getDimensionPixelSize(R.dimen.seek_bar_thumb_size)

    private var textColor: Int = ResourcesCompat.getColor(cxt.resources, R.color.colorYellow, null)

    private var thumbColor: Int = ResourcesCompat.getColor(cxt.resources, R.color.colorPrimaryDark, null)

    internal val intValue: Int
        get() = value.toInt()

    private fun createThumbDrawable(): TextDrawable = TextDrawable.builder()
            .beginConfig()
            .width(thumbWidth)
            .height(thumbHeight)
            .textColor(textColor)
            .endConfig()
            .buildRound(value.toString(), thumbColor)

    internal fun createFromPrefs(cxt: Context, key: String, defaultValue: Float): Adjust =
            Adjust(cxt, key, PreferenceManager.getDefaultSharedPreferences(cxt).getFloat(key, defaultValue))

    internal fun save(cxt: Context) {
        val prefs = PreferenceManager.getDefaultSharedPreferences(cxt)
        val edit = prefs.edit()
        edit.putFloat(key, value)
        SharedPreferencesCompat.EditorCompat.getInstance()
                .apply(edit)
    }

    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        value = progress / 100.0f
        seekBar?.thumb = createThumbDrawable()
    }

    override fun onStartTrackingTouch(seekBar: SeekBar?) {
    }

    override fun onStopTrackingTouch(seekBar: SeekBar?) {
    }
}