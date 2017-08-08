package com.qiaoqiao.core.camera.ui

import android.app.Activity
import android.content.Intent
import android.view.GestureDetector
import android.view.MotionEvent
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.vision.barcode.Barcode
import com.qiaoqiao.core.camera.barcode.BarcodeGraphic
import com.qiaoqiao.core.camera.barcode.GraphicOverlay
import com.qiaoqiao.core.product.ui.ProductListActivity
import java.lang.ref.WeakReference

internal class CaptureGestureListener(cxt: Activity, private val graphicOverlay: GraphicOverlay<BarcodeGraphic>) : GestureDetector.SimpleOnGestureListener() {
    private val activity: WeakReference<Activity> = WeakReference(cxt)

    override fun onSingleTapConfirmed(e: MotionEvent): Boolean {
        return onTap(e.rawX, e.rawY) || super.onSingleTapConfirmed(e)
    }

    internal fun onTap(rawX: Float, rawY: Float): Boolean {
        val location = IntArray(2)
        graphicOverlay.getLocationOnScreen(location)
        val x = (rawX - location[0]) / graphicOverlay.widthScaleFactor
        val y = (rawY - location[1]) / graphicOverlay.heightScaleFactor

        // Find the barcode whose center is closest to the tapped point.
        var best: Barcode? = null
        var bestDistance = java.lang.Float.MAX_VALUE
        for (graphic in graphicOverlay.graphics) {
            val barcode = graphic.barcode
            if (barcode.boundingBox.contains(x.toInt(), y.toInt())) {
                // Exact hit, no need to keep looking.
                best = barcode
                break
            }
            val dx = x - barcode.boundingBox.centerX()
            val dy = y - barcode.boundingBox.centerY()
            val distance = dx * dx + dy * dy  // actually squared distance
            if (distance < bestDistance) {
                best = barcode
                bestDistance = distance
            }
        }

        if (best != null && activity.get() != null) {
            val act = activity.get()
            val data = Intent()
            data.putExtra("barcode", best)
            act?.setResult(CommonStatusCodes.SUCCESS, data)
//            act?.finish()
            ProductListActivity.showInstance(act!!, best.displayValue)
            return true
        }
        return false
    }
}