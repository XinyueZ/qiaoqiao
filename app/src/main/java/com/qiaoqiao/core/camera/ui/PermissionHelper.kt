package com.qiaoqiao.core.camera.ui

import android.Manifest
import android.Manifest.permission.*
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import com.qiaoqiao.R
import com.qiaoqiao.settings.RC_CAMERA_PERMISSIONS
import com.qiaoqiao.settings.RC_FINE_LOCATION_PERMISSIONS
import com.qiaoqiao.settings.RC_READ_EXTERNAL_STORAGE_PERMISSIONS
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions
import java.lang.ref.Reference
import java.lang.ref.WeakReference

internal object PermissionHelper : EasyPermissions.PermissionCallbacks {

    private lateinit var activity: Reference<CameraActivity>

    fun setCameraActivity(cameraActivity: CameraActivity) {
        activity = WeakReference(cameraActivity)
    }

    @AfterPermissionGranted(RC_CAMERA_PERMISSIONS)
    fun requireCameraPermission() {
        if (activity.get() == null) return
        val act = activity.get() as CameraActivity
        if (EasyPermissions.hasPermissions(act, CAMERA)) {
            CameraSetup.setup(act)
        } else {
            EasyPermissions.requestPermissions(act, act.getString(R.string.permission_relation_to_camera_text), RC_CAMERA_PERMISSIONS, CAMERA)
        }
    }

    @AfterPermissionGranted(RC_READ_EXTERNAL_STORAGE_PERMISSIONS)
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

    @AfterPermissionGranted(RC_FINE_LOCATION_PERMISSIONS)
    fun requireFineLocationPermission() {
        if (activity.get() == null) return
        val act = activity.get() as CameraActivity
        val frg = act.mSnapshotPlacesFragment as Fragment
        if (EasyPermissions.hasPermissions(act, ACCESS_FINE_LOCATION)) {
            act.supportFragmentManager.beginTransaction()
                    .setCustomAnimations(R.anim.slide_in_from_right, R.anim.slide_out_to_left, android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                    .add(R.id.container,
                            frg,
                            frg.javaClass.name)
                    .addToBackStack(null)
                    .commit()
        } else {
            // Ask for one permission
            EasyPermissions.requestPermissions(act, act.getString(R.string.permission_relation_to_location_text), RC_FINE_LOCATION_PERMISSIONS, ACCESS_FINE_LOCATION)
        }
    }

    override fun onPermissionsGranted(i: Int, list: List<String>) {
        if (activity.get() == null) return
        val act = activity.get() as CameraActivity
        if (list.contains(Manifest.permission.READ_EXTERNAL_STORAGE)) {
            act.openLocalDir()
        }
        if (list.contains(CAMERA)) {
            CameraSetup.setup(act)
        }
    }

    override fun onPermissionsDenied(i: Int, perms: List<String>) {
        if (activity.get() == null) return
        val act = activity.get() as AppCompatActivity
        if (EasyPermissions.somePermissionPermanentlyDenied(act, perms)) {
            AppSettingsDialog.Builder(act).setPositiveButton(R.string.permission_setting)
                    .build()
                    .show()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }
}
