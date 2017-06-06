package com.qiaoqiao.core.camera.awareness.ui

import android.content.Context
import android.support.v4.content.SharedPreferencesCompat.EditorCompat.getInstance
import android.support.v4.content.res.ResourcesCompat
import android.support.v7.preference.PreferenceManager.getDefaultSharedPreferences
import android.widget.SeekBar
import com.amulyakhare.textdrawable.TextDrawable
import com.qiaoqiao.R

class Adjust(val cxt: Context, val key: String, var value: Int) :
        SeekBar.OnSeekBarChangeListener {
    private var thumbWidth: Int = cxt.resources.getDimensionPixelSize(R.dimen.seek_bar_thumb_size)

    private var thumbHeight: Int = cxt.resources.getDimensionPixelSize(R.dimen.seek_bar_thumb_size)

    private var textColor: Int = ResourcesCompat.getColor(cxt.resources, R.color.colorYellow, null)

    private var thumbColor: Int = ResourcesCompat.getColor(cxt.resources, R.color.colorPrimaryDark, null)

    fun createThumbDrawable(): TextDrawable = TextDrawable.builder()
            .beginConfig()
            .width(thumbWidth)
            .height(thumbHeight)
            .textColor(textColor)
            .endConfig()
            .buildRound(value.toString(), thumbColor)

    companion object Factory {
        fun createAdjust(cxt: Context, key: String, defaultValue: Int): Adjust =
                Adjust(cxt, key, getDefaultSharedPreferences(cxt).getInt(key, defaultValue))

    }

    private fun save(cxt: Context) {
        val prefs = getDefaultSharedPreferences(cxt)
        val edit = prefs.edit()
        edit.putInt(key, value)
        getInstance()
                .apply(edit)
    }

    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        value = progress
        save(cxt)
        seekBar?.thumb = createThumbDrawable()
    }

    override fun onStartTrackingTouch(seekBar: SeekBar?) {
    }

    override fun onStopTrackingTouch(seekBar: SeekBar?) {
    }
}