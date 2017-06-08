package com.qiaoqiao.core.splash.ui

import android.Manifest.permission.CAMERA
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.GlideDrawable
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.qiaoqiao.R
import com.qiaoqiao.core.camera.ui.CameraActivity
import com.qiaoqiao.core.splash.SplashContract
import com.qiaoqiao.core.splash.SplashPresenter
import com.qiaoqiao.databinding.FragmentLaunchImageBinding
import com.qiaoqiao.utils.SystemUiHelper
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions
import java.lang.Exception

private const val RC_PERMISSIONS = 123

class LaunchImageFragment : Fragment(), SplashContract.LaunchImageView, EasyPermissions.PermissionCallbacks, RequestListener<Uri, GlideDrawable> {

    private var presenter: SplashContract.Presenter? = null
    private var binding: FragmentLaunchImageBinding? = null

    companion object Factory {
        fun newInstance(cxt: Context): LaunchImageFragment = Fragment.instantiate(cxt, LaunchImageFragment::class.java.name) as LaunchImageFragment
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val uiHelper = SystemUiHelper(activity, SystemUiHelper.LEVEL_IMMERSIVE, 0)
        uiHelper.hide()
        super.onCreate(savedInstanceState)
        val binding = FragmentLaunchImageBinding.inflate(inflater!!, container, false)
        binding.uiHelper = uiHelper
        this.binding = binding
        return binding.root
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.presenter?.loadLaunchImage()
    }

    override fun getBinding(): FragmentLaunchImageBinding? = this.binding

    override fun showLaunchImage(uri: Uri) {
        Glide.with(this)
                .load(uri)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .skipMemoryCache(false)
                .crossFade()
                .listener(this)
                .into(binding?.launchImageIv)
    }

    override fun onException(e: Exception?, model: Uri?, target: Target<GlideDrawable>?, isFirstResource: Boolean): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onResourceReady(resource: GlideDrawable?, model: Uri?, target: Target<GlideDrawable>?, isFromMemoryCache: Boolean, isFirstResource: Boolean): Boolean {
        requirePermission()
        return false
    }

    override fun onStop() {
        super.onStop()
        Glide.clear(binding?.launchImageIv)
    }

    override fun showLaunchImage(data: ByteArray) {
        binding?.launchImageIv?.setImageBitmap(BitmapFactory.decodeByteArray(data, data.size, 0))
        requirePermission()
    }

    override fun setPresenter(presenter: SplashPresenter) {
        this.presenter = presenter
    }

    private fun goToHome() {
        CameraActivity.showInstance(activity)
        activity.finish()
    }

    @AfterPermissionGranted(RC_PERMISSIONS)
    override fun requirePermission() {
        if (hasPermission()) {
            goToHome()
        } else {
            // Ask for one permission
            EasyPermissions.requestPermissions(this, getString(R.string.permission_relation_to_camera_text), RC_PERMISSIONS, CAMERA)
        }
    }

    @SuppressLint("InlinedApi")
    private fun hasPermission(): Boolean {
        return EasyPermissions.hasPermissions(activity, CAMERA)
    }

    override fun onPermissionsDenied(i: Int, list: List<String>) {
        if (!hasPermission()) {
            AppSettingsDialog.Builder(this).setPositiveButton(R.string.permission_setting)
                    .setNegativeButton(getString(R.string.exit_app)) { dialogInterface, i1 -> activity.supportFinishAfterTransition() }
                    .build()
                    .show()
        } else {
            goToHome()
        }
    }

    override fun onPermissionsGranted(i: Int, list: List<String>) {
        goToHome()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }
}