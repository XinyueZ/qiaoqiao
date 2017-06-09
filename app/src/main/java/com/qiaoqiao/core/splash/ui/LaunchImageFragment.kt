package com.qiaoqiao.core.splash.ui

import android.Manifest.permission.CAMERA
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.qiaoqiao.R
import com.qiaoqiao.core.camera.ui.CameraActivity
import com.qiaoqiao.core.splash.SplashContract
import com.qiaoqiao.core.splash.SplashPresenter
import com.qiaoqiao.databinding.LaunchImageBinding
import com.qiaoqiao.utils.ImageUtils
import com.qiaoqiao.utils.LL
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions
import java.lang.Exception
import java.util.concurrent.TimeUnit

private const val RC_PERMISSIONS = 123

class LaunchImageFragment : Fragment(), SplashContract.LaunchImageView, EasyPermissions.PermissionCallbacks, RequestListener<Uri, Bitmap> {

    private var presenter: SplashContract.Presenter? = null
    private lateinit var binding: LaunchImageBinding

    companion object {
        fun newInstance(cxt: Context): LaunchImageFragment = Fragment.instantiate(cxt, LaunchImageFragment::class.java.name) as LaunchImageFragment
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreate(savedInstanceState)
        val binding = LaunchImageBinding.inflate(inflater!!, container, false)
        this.binding = binding
        return binding.root
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.presenter?.loadLaunchImage(context)
    }

    override fun getBinding(): LaunchImageBinding = this.binding

    override fun showLaunchImage(uri: Uri) {
        Glide.with(this)
                .load(uri).asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .skipMemoryCache(false)
                .crossFade()
                .listener(this)
                .into(binding.launchImageIv)
    }

    override fun onResourceReady(resource: Bitmap?, model: Uri?, target: Target<Bitmap>?, isFromMemoryCache: Boolean, isFirstResource: Boolean): Boolean {
        requirePermission()

        if (resource == null) return true
        presenter?.saveLoadedLaunchImage(ImageUtils.convertImage2Bytes(resource))
        return false
    }

    override fun onException(e: Exception?, model: Uri?, target: Target<Bitmap>?, isFirstResource: Boolean): Boolean {
        return false
    }

    override fun showLaunchImage(data: ByteArray) {
        binding.launchImageIv.setImageBitmap(BitmapFactory.decodeByteArray(data, 0, data.size))
        Handler().postDelayed({ -> requirePermission() }, TimeUnit.SECONDS.toMillis(3))
    }

    override fun onStop() {
        super.onStop()
        Glide.clear(binding.launchImageIv)
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