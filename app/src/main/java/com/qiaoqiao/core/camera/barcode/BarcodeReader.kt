package com.qiaoqiao.core.camera.barcode

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.util.Log
import android.widget.Toast
import com.google.android.gms.vision.MultiProcessor
import com.google.android.gms.vision.barcode.BarcodeDetector
import com.qiaoqiao.R
import com.qiaoqiao.utils.LL

class BarcodeReader(
        private val graphicOverlay: GraphicOverlay<BarcodeGraphic>) {
    fun start(cxt: Context) {
        val barcodeDetector = BarcodeDetector.Builder(cxt).build()
        val barcodeFactory = BarcodeTrackerFactory(graphicOverlay)
        barcodeDetector.setProcessor(MultiProcessor.Builder(barcodeFactory).build())

        if (!barcodeDetector.isOperational){
            LL.w("Detector dependencies are not yet available.")

            val lowstorageFilter = IntentFilter(Intent.ACTION_DEVICE_STORAGE_LOW)
            val hasLowStorage = cxt.registerReceiver(null, lowstorageFilter) != null

            if (hasLowStorage) {
                Toast.makeText(cxt, R.string.low_storage_error, Toast.LENGTH_LONG).show()
            }
        }
    }
}