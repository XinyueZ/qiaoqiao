package com.qiaoqiao.core.camera.ui

import android.support.v4.view.GravityCompat

internal object BackPressHandler {
    fun onBackPressed(cxt: CameraActivity): Boolean {
        with(cxt) {
            binding?.let {
                if (it.drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    it.drawerLayout.closeDrawer(GravityCompat.START)
                    return true
                }

                if (alreadyOnBottom) {
                    it.viewpager.setCurrentItem(0, true)
                    showCameraOnly()
                    return true
                }
            }
        }
        return false
    }
}
