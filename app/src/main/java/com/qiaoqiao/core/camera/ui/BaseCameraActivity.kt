package com.qiaoqiao.core.camera.ui

import android.os.Bundle
import android.support.v4.content.ContextCompat
import com.afollestad.materialcamera.internal.BaseCaptureActivity
import com.afollestad.materialcamera.internal.Camera2Fragment
import com.afollestad.materialcamera.internal.CameraFragment
import com.afollestad.materialcamera.internal.CameraIntentKey
import com.afollestad.materialcamera.util.CameraUtil
import com.qiaoqiao.R

abstract class BaseCameraActivity : BaseCaptureActivity() {
    private var stillShot = true
    private var _args: Bundle? = null
    private val args: Bundle
        get() {
            if (_args == null) {
                _args = Bundle()
                _args!!.putInt(CameraIntentKey.PRIMARY_COLOR, ContextCompat.getColor(applicationContext, R.color.colorPrimary))
                _args!!.putBoolean(CameraIntentKey.STILL_SHOT, stillShot)
            }
            return _args ?: throw AssertionError("Set to null by another thread")
        }

    override fun getFragment() = (if (CameraUtil.hasCamera2(this, stillShot)) {
        Camera2Fragment.newInstance(applicationContext, args)
    } else {
        CameraFragment.newInstance(applicationContext, args)
    })!!

    override fun createFragment() = fragment
}