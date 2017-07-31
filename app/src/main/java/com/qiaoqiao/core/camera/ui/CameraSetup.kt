package com.qiaoqiao.core.camera.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.content.IntentFilter
import android.hardware.Camera
import android.support.v4.view.GestureDetectorCompat
import android.view.ScaleGestureDetector
import android.widget.Toast
import com.google.android.gms.vision.MultiProcessor
import com.google.android.gms.vision.barcode.Barcode
import com.google.android.gms.vision.barcode.BarcodeDetector
import com.qiaoqiao.R
import com.qiaoqiao.core.camera.barcode.BarcodeGraphic
import com.qiaoqiao.core.camera.barcode.BarcodeTrackerFactory
import com.qiaoqiao.core.camera.barcode.CameraSource
import com.qiaoqiao.core.camera.barcode.GraphicOverlay
import com.qiaoqiao.utils.LL
import java.io.IOException

@SuppressLint("MissingPermission")
internal object CameraSetup {
    fun setup(cxt: CameraActivity) {
        with(cxt) {
            val barcodeDetector = BarcodeDetector.Builder(applicationContext).build()
            val barcodeFactory = BarcodeTrackerFactory(mBinding.barcodeDetectorOverlay as GraphicOverlay<BarcodeGraphic>?)
            barcodeDetector.setProcessor(MultiProcessor.Builder<Barcode>(barcodeFactory).build())

            if (!barcodeDetector.isOperational) {
                val lowstorageFilter = IntentFilter(Intent.ACTION_DEVICE_STORAGE_LOW)
                val hasLowStorage = registerReceiver(null, lowstorageFilter) != null
                if (hasLowStorage) {
                    Toast.makeText(this, R.string.low_storage_error, Toast.LENGTH_LONG)
                            .show()
                }
            }
            val cameraSource = CameraSource.Builder(applicationContext, barcodeDetector)
                    .setFacing(CameraSource.CAMERA_FACING_BACK)
                    .setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)
                    //.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH)
                    .setRequestedFps(15.0f)
                    .build()
            try {
                mBinding.cameraSource = cameraSource
                mBinding.scaleDetector = ScaleGestureDetector(this, ScaleListener(cameraSource))
                mBinding.gestureDetector = GestureDetectorCompat(this, CaptureGestureListener(this, mBinding.barcodeDetectorOverlay as GraphicOverlay<BarcodeGraphic>))
                mBinding.collapsingToolbar.setOnTouchListener({ _, e ->
                    mBinding?.let {
                        val b = it.scaleDetector!!.onTouchEvent(e)
                        val c = it.gestureDetector!!.onTouchEvent(e)
                        b || c
                    } ?: let {
                        false
                    }
                })
                mBinding.preview.start(cameraSource, mBinding.barcodeDetectorOverlay)
            } catch (e: IOException) {
                LL.w(e.toString())
            }
        }
    }
}