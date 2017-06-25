package com.qiaoqiao.core.camera.vision.ui

import android.support.v4.app.Fragment
import android.text.TextUtils
import android.view.View
import com.qiaoqiao.core.camera.vision.model.VisionEntity
import com.qiaoqiao.core.detail.ui.DetailActivity
import com.qiaoqiao.core.location.MapActivity

abstract class AbstractVisionFragment : Fragment() {
    protected fun openDetail(entity: VisionEntity, transitionView: View) {
        if (activity == null || !userVisibleHint) return
        val latLng = entity.location.toLatLng()
        if (TextUtils.equals("LANDMARK_DETECTION", entity.readableName) && latLng != null) {
            MapActivity.showInstance(activity, latLng, transitionView)
            return
        }
        if ((TextUtils.equals("WEB_DETECTION", entity.readableName) || TextUtils.equals("LOGO_DETECTION", entity.readableName) || TextUtils.equals("LABEL_DETECTION",
                entity.readableName)) && !TextUtils
                .isEmpty(entity.description.descriptionText)) {
            DetailActivity.showInstance(activity, entity.description.descriptionText, transitionView)
        }
    }
}