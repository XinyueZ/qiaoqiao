package com.qiaoqiao.core.camera.ui

import android.app.Activity
import android.content.Intent
import android.support.design.widget.AppBarLayout
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v4.view.ViewCompat
import android.view.Menu
import android.view.View
import com.qiaoqiao.R
import com.qiaoqiao.core.camera.awareness.REQ_SETTING_LOCATING
import com.qiaoqiao.core.camera.crop.model.CropSource
import com.qiaoqiao.core.camera.ui.CameraActivity.REQ_FILE_SELECTOR

internal object Scenario {
    fun adjustUIForDifferentFragmentScenario(cxt: CameraActivity, menu: Menu) {
        with(cxt.binding) {
            val isStillshot = true
            val isSnapshotPlacesThere = (cxt.mSnapshotPlacesFragment as Fragment).isAdded
            val isCropThere = (cxt.mCropFragment as Fragment).isAdded

            navView.menu
                    .findItem(R.id.action_places).isVisible = !isSnapshotPlacesThere
            navView.menu
                    .findItem(R.id.action_from_local).isVisible = !isSnapshotPlacesThere
            navView.menu
                    .findItem(R.id.action_from_web).isVisible = !isSnapshotPlacesThere

            menu.findItem(R.id.action_crop_rotate).isVisible = isCropThere && !isSnapshotPlacesThere
            menu.findItem(R.id.action_video).isVisible = !isCropThere && !isSnapshotPlacesThere && isStillshot
            menu.findItem(R.id.action_photo).isVisible = !isCropThere && !isSnapshotPlacesThere && !isStillshot

            ViewCompat.animate(expandMoreBtn)
                    .alpha((if (!isCropThere && !isSnapshotPlacesThere)
                        1
                    else
                        0).toFloat())
                    .start()
            ViewCompat.animate(expandLessBtn)
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

    fun onOffsetChanged(cxt: CameraActivity, appBarLayout: AppBarLayout, verticalOffset: Int) {
        with(cxt) {
            mOnBottom = Math.abs(verticalOffset) == appBarLayout.totalScrollRange
            if (!mOnBottom) {
                mBinding.expandMoreBtn.visibility = View.VISIBLE
                mBinding.expandLessBtn.visibility = View.GONE
            } else {
                mBinding.expandMoreBtn.visibility = View.GONE
                mBinding.expandLessBtn.visibility = View.VISIBLE
            }
        }
    }

    fun onActivityResult(cxt: CameraActivity, requestCode: Int, resultCode: Int, data: Intent?): Boolean {
        with(cxt) {
            when (requestCode) {
                REQ_WEB_LINK -> if (data != null && data.data != null) {
                    openCrop(CropSource(data.data))
                    return true
                }
                REQ_FILE_SELECTOR -> if (resultCode == Activity.RESULT_OK && data != null && data.data != null) {
                    openCrop(CropSource(data.data))
                    return true
                }
                REQ_INVITE -> if (resultCode != Activity.RESULT_OK) {
                    mSnackbar = Snackbar.make(mBinding.root, R.string.invitation_send_failed, Snackbar.LENGTH_LONG)
                            .setAction(android.R.string.cancel, this)
                    mSnackbar?.show()
                    return true
                }
                REQ_SETTING_LOCATING -> {
                    mAwarenessPresenter.locating(this)
                    return true
                }
            }
        }
        return false
    }
}