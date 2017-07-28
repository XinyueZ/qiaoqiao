package com.qiaoqiao.core.camera.ui

import com.afollestad.materialcamera.internal.Camera2Fragment
import com.afollestad.materialcamera.internal.CameraFragment

class Camera1Activity : CameraActivity() {
    override fun getFragment() = CameraFragment.newInstance()
}

class Camera2Activity : CameraActivity() {
    override fun getFragment() = Camera2Fragment.newInstance()
}