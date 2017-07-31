package com.qiaoqiao.core.camera.ui

import android.support.v4.view.GravityCompat

internal object BackPressHandler {
    fun onBackPressed(cxt: CameraActivity): Boolean {
        with(cxt) {
            if (mBinding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
                mBinding.drawerLayout.closeDrawer(GravityCompat.START)
                return true
            }

            if (mOnBottom) {
                mBinding.viewpager.setCurrentItem(0, true)
                showCameraOnly()
                return true
            }
        }
        return false
    }
}
