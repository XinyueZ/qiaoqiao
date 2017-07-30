package com.qiaoqiao.core.camera.ui

import android.view.ScaleGestureDetector
import com.qiaoqiao.core.camera.barcode.CameraSource

internal class ScaleListener(private val cameraSource: CameraSource) : ScaleGestureDetector.SimpleOnScaleGestureListener() {
    override fun onScaleBegin(detector: ScaleGestureDetector?) = true

    override fun onScaleEnd(detector: ScaleGestureDetector?) {
        if (detector != null)
            cameraSource.doZoom(detector.scaleFactor)
    }

    override fun onScale(detector: ScaleGestureDetector?) = false
}