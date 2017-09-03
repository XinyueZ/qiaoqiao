package com.qiaoqiao.core.camera.ui

import android.view.View
import com.qiaoqiao.R

internal object ClickHandler {
    fun onClick(cxt: CameraActivity, v: View) {
        with(cxt) {
            when (v.id) {
                R.id.expand_less_btn -> showCameraOnly()
                R.id.expand_more_btn -> showVisionOnly()
                else -> snackbar?.dismiss()

            }
        }
    }
}