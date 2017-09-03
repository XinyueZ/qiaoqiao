package com.qiaoqiao.core.camera.ui

import android.app.Activity
import android.content.Intent
import android.support.design.widget.AppBarLayout
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v4.view.ViewCompat
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.google.firebase.auth.FirebaseAuth
import com.qiaoqiao.R
import com.qiaoqiao.core.camera.awareness.REQ_SETTING_LOCATING
import com.qiaoqiao.core.camera.crop.model.CropSource

internal object Scenario {
    fun adjustUIForDifferentFragmentScenario(cxt: CameraActivity, menu: Menu) {
        cxt.binding?.let {
            with(it) {
                val isStillshot = true
                val isSnapshotPlacesThere = (cxt.mSnapshotPlacesFragment as Fragment).isAdded
                val isCropThere = (cxt.mCropFragment as Fragment).isAdded

                with(navView.menu) {
                    //Navigation-drawer
                    setMenuVisible(findItem(R.id.action_places), !isSnapshotPlacesThere)
                    setMenuVisible(findItem(R.id.action_from_local), !isSnapshotPlacesThere)
                    setMenuVisible(findItem(R.id.action_from_web), !isSnapshotPlacesThere)
                }
                //Normal menu
                setMenuVisible(menu.findItem(R.id.action_crop_rotate), isCropThere && !isSnapshotPlacesThere)
                //Views
                val show = (!isCropThere && !isSnapshotPlacesThere)
                val fireVision = FirebaseAuth.getInstance()
                        .currentUser != null
                animateViews(captureFab, show)
                animateViews(expandMoreBtn, show && fireVision)
                animateViews(expandLessBtn, show && fireVision)
                animateViews(cameraFaceFab, show)
                animateViews(flashFab, show)
                animateViews(cxt.supportFragmentManager.findFragmentById(R.id.stackview_history_fg).view, show)
            }
        }
    }

    private fun setMenuVisible(item: MenuItem, show: Boolean) {
        item.isVisible = show
    }

    private fun animateViews(view: View?, show: Boolean) {
        ViewCompat.animate(view)
                .alpha((if (show)
                    1
                else
                    0).toFloat())
                .start()
    }

    fun onOffsetChanged(cxt: CameraActivity, appBarLayout: AppBarLayout, verticalOffset: Int) {
        with(cxt) {
            cxt.binding?.let {
                alreadyOnBottom = Math.abs(verticalOffset) == appBarLayout.totalScrollRange
                if (!alreadyOnBottom) {
                    it.expandMoreBtn.visibility = View.VISIBLE
                    it.expandLessBtn.visibility = View.GONE
                } else {
                    it.expandMoreBtn.visibility = View.GONE
                    it.expandLessBtn.visibility = View.VISIBLE
                }
            }
        }
    }

    fun onActivityResult(cxt: CameraActivity, requestCode: Int, resultCode: Int, data: Intent?): Boolean {
        with(cxt) {
            binding?.let {
                when (requestCode) {
                    REQ_WEB_LINK -> when {
                        data != null && data.data != null -> {
                            openCrop(CropSource(data.data))
                            return true
                        }
                    }
                    REQ_FILE_SELECTOR -> when {
                        resultCode == Activity.RESULT_OK && data != null && data.data != null -> {
                            openCrop(CropSource(data.data))
                            return true
                        }
                    }
                    REQ_INVITE -> {
                        when {
                            resultCode != Activity.RESULT_OK -> {
                                snackbar = Snackbar.make(it.root, R.string.invitation_send_failed, Snackbar.LENGTH_LONG)
                                        .setAction(android.R.string.cancel, this)
                                snackbar?.show()
                                return true
                            }
                            else -> {
                            }
                        }
                    }
                    REQ_SETTING_LOCATING -> {
                        mAwarenessPresenter.locating(this)
                        return true
                    }
                }
            }
        }
        return false
    }
}