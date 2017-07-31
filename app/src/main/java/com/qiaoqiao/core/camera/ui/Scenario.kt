package com.qiaoqiao.core.camera.ui

import android.support.v4.app.Fragment
import android.support.v4.view.ViewCompat
import android.view.Menu
import com.qiaoqiao.R

internal object Scenario {
    fun adjustUIForDifferentFragmentScenario(cxt: CameraActivity, menu: Menu) {
        with(cxt.binding) {
            val isStillshot = true
            val isSnapshotPlacesThere = (cxt.mSnapshotPlacesFragment as Fragment).isAdded
            val isCropThere = (cxt.mCropFragment as Fragment).isAdded
            this.navView.menu
                    .findItem(R.id.action_places).isVisible = !isSnapshotPlacesThere
            this.navView.menu
                    .findItem(R.id.action_from_local).isVisible = !isSnapshotPlacesThere
            this.navView.menu
                    .findItem(R.id.action_from_web).isVisible = !isSnapshotPlacesThere

            menu.findItem(R.id.action_crop_rotate).isVisible = isCropThere && !isSnapshotPlacesThere
            menu.findItem(R.id.action_video).isVisible = !isCropThere && !isSnapshotPlacesThere && isStillshot
            menu.findItem(R.id.action_photo).isVisible = !isCropThere && !isSnapshotPlacesThere && !isStillshot

            ViewCompat.animate(this.expandMoreBtn)
                    .alpha((if (!isCropThere && !isSnapshotPlacesThere)
                        1
                    else
                        0).toFloat())
                    .start()
            ViewCompat.animate(this.expandLessBtn)
                    .alpha((if (!isCropThere && !isSnapshotPlacesThere)
                        1
                    else
                        0).toFloat())
                    .start()

            val view = cxt.supportFragmentManager.findFragmentById(R.id.stackview_history_fg)
                    .view
            ViewCompat.animate(view)
                    .alpha((if (!isCropThere && !isSnapshotPlacesThere)
                        1
                    else
                        0).toFloat())
                    .start()
        }
    }
}