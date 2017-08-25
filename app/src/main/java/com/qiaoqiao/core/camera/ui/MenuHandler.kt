package com.qiaoqiao.core.camera.ui

import android.view.MenuItem
import com.qiaoqiao.R
import com.qiaoqiao.core.confidence.ui.ConfidenceDialogFragment
import com.qiaoqiao.licenses.LicensesActivity
import com.qiaoqiao.settings.SettingsActivity

internal object MenuHandler {
    fun onOptionsItemSelected(cxt: CameraActivity, item: MenuItem): Boolean {
        if (cxt.mDrawerToggle != null && cxt.mDrawerToggle.onOptionsItemSelected(item)) {
            return true
        }
        when (item.itemId) {
            R.id.action_video -> {
                CameraActivity.showInstance(cxt, true)
                cxt.finish()
                return true
            }
            R.id.action_photo -> {
                CameraActivity.showInstance(cxt, true)
                cxt.finish()
                return true
            }
            R.id.action_crop_rotate -> {
                cxt.mCropFragment.rotate()
                return true
            }
        }
        return false
    }

    fun onNavigationItemSelected(cxt: CameraActivity, item: MenuItem) {
        cxt.binding.drawerLayout.closeDrawers()
        when (item.itemId) {
            R.id.action_from_local -> cxt.showLoadFromLocal(null)
            R.id.action_from_web -> cxt.showInputFromWeb(null)
            R.id.action_places -> cxt.mPermissionHelper.requireFineLocationPermission()
            R.id.action_confidence -> (cxt.mConfidenceFragment as ConfidenceDialogFragment).show(cxt.supportFragmentManager, null)
            R.id.action_app_invite -> AppInvitation.sendAppInvitation(cxt)
            R.id.action_settings -> SettingsActivity.showInstance(cxt)
            R.id.action_source_license -> LicensesActivity.showInstance(cxt)
        }
    }
}