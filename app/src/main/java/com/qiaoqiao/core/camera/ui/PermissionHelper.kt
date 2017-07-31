package com.qiaoqiao.core.camera.ui

import android.Manifest
import android.Manifest.permission.*
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import com.qiaoqiao.R
import com.qiaoqiao.settings.RC_CAMERA_PERMISSIONS
import com.qiaoqiao.settings.RC_FINE_LOCATION_PERMISSIONS
import com.qiaoqiao.settings.RC_READ_EXTERNAL_STORAGE_PERMISSIONS
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions
import java.lang.ref.WeakReference

internal class PermissionHelper(cxt: CameraActivity) {
    private val activity: WeakReference<CameraActivity> = WeakReference(cxt)

    fun requireCameraPermission() {
        if (activity.get() == null) return
        val act = activity.get() as CameraActivity
        if (EasyPermissions.hasPermissions(act, CAMERA)) {
            act.setupCamera()
        } else {
            EasyPermissions.requestPermissions(act, act.getString(R.string.permission_relation_to_camera_text), RC_CAMERA_PERMISSIONS, CAMERA)
        }
    }

    fun requireReadExternalStoragePermission() {
        if (activity.get() == null) return
        val act = activity.get() as CameraActivity
        if (EasyPermissions.hasPermissions(act, READ_EXTERNAL_STORAGE)) {
            act.openLocalDir()
        } else {
            // Ask for one permission
            EasyPermissions.requestPermissions(act, act.getString(R.string.permission_relation_to_read_external_storage_text), RC_READ_EXTERNAL_STORAGE_PERMISSIONS, READ_EXTERNAL_STORAGE)
        }
    }

    fun requireFineLocationPermission(fragment: Fragment) {
        if (activity.get() == null) return
        val act = activity.get() as AppCompatActivity
        if (EasyPermissions.hasPermissions(act, ACCESS_FINE_LOCATION)) {
            act.supportFragmentManager.beginTransaction()
                    .setCustomAnimations(R.anim.slide_in_from_right, R.anim.slide_out_to_left, android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                    .add(R.id.container,
                            fragment,
                            fragment.javaClass.name)
                    .addToBackStack(null)
                    .commit()
        } else {
            // Ask for one permission
            EasyPermissions.requestPermissions(act, act.getString(R.string.permission_relation_to_location_text), RC_FINE_LOCATION_PERMISSIONS, ACCESS_FINE_LOCATION)
        }
    }

    fun onPermissionsGranted(i: Int, list: List<String>) {
        if (activity.get() == null) return
        val act = activity.get() as CameraActivity
        if (list.contains(Manifest.permission.READ_EXTERNAL_STORAGE)) {
            act.openLocalDir()
        }
        if (list.contains(CAMERA)) {
            act.setupCamera()
        }
    }

    fun onPermissionsDenied(i: Int, perms: List<String>) {
        if (activity.get() == null) return
        val act = activity.get() as AppCompatActivity
        if (EasyPermissions.somePermissionPermanentlyDenied(act, perms)) {
            AppSettingsDialog.Builder(act).setPositiveButton(R.string.permission_setting)
                    .build()
                    .show()
        }
    }
}
