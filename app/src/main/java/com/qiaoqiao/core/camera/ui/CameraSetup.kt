package com.qiaoqiao.core.camera.ui

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.hardware.Camera
import android.os.Vibrator
import android.support.v4.view.GestureDetectorCompat
import android.text.TextUtils
import android.view.ScaleGestureDetector
import android.widget.Toast
import com.google.android.gms.vision.MultiProcessor
import com.google.android.gms.vision.barcode.Barcode
import com.google.android.gms.vision.barcode.BarcodeDetector
import com.google.firebase.auth.FirebaseAuth
import com.qiaoqiao.R
import com.qiaoqiao.app.PrefsKeys
import com.qiaoqiao.core.camera.barcode.BarcodeGraphic
import com.qiaoqiao.core.camera.barcode.BarcodeTrackerFactory
import com.qiaoqiao.core.camera.barcode.CameraSource
import com.qiaoqiao.core.camera.barcode.GraphicOverlay
import com.qiaoqiao.core.splash.ui.ConnectGoogleActivity
import com.qiaoqiao.utils.LL
import java.io.IOException

@SuppressLint("MissingPermission")
internal object CameraSetup {
    private lateinit var vibrator: Vibrator

    fun setup(cxt: CameraActivity) {
        with(cxt) {
            vibrator = cxt.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
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
            val face=if(cxt.isBackCamera) CameraSource.CAMERA_FACING_BACK else CameraSource.CAMERA_FACING_FRONT
            val cameraSource = CameraSource.Builder(applicationContext, barcodeDetector)
                    .setFacing(face)
                    .setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)
                    .setFlashMode(Camera.Parameters.FLASH_MODE_OFF)
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
            mBinding.captureFab.setOnClickListener {
                if (FirebaseAuth.getInstance().currentUser == null)
                    ConnectGoogleActivity.showInstance(cxt, false)
                else
                    cameraSource.takePicture({ vibrator.vibrate(PrefsKeys.VIB_LNG) }, {
                        mCameraPresenter.capturedByteArray(this, it)
                    })
            }
            mBinding.flashFab.setOnClickListener {
                vibrator.vibrate(PrefsKeys.VIB_LNG)
                mBinding.cameraSource?.let {
                    val mode = if (TextUtils.equals(it.flashMode, Camera.Parameters.FLASH_MODE_ON))
                        Camera.Parameters.FLASH_MODE_OFF else Camera.Parameters.FLASH_MODE_ON
                    if (it.setFlashMode(mode))
                        mBinding.flashFab.setImageResource(if (TextUtils.equals(it.flashMode, Camera.Parameters.FLASH_MODE_OFF))
                            R.drawable.ic_flash_on else R.drawable.ic_flash_off)
                }
            }

            mBinding.cameraFaceFab.setOnClickListener {
                vibrator.vibrate(PrefsKeys.VIB_LNG)
                mBinding.cameraSource?.let {
                    CameraActivity.showInstance(cxt, it.cameraFacing == CameraSource.CAMERA_FACING_FRONT)
                    it.stop()
                    it.release()
                    cxt.finish()
                }
            }
        }
    }
}