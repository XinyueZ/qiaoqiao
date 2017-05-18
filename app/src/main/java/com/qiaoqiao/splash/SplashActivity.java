package com.qiaoqiao.splash;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.qiaoqiao.R;
import com.qiaoqiao.camera.ui.CameraActivity;
import com.qiaoqiao.utils.SystemUiHelper;

import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

import static android.Manifest.permission.CAMERA;

public final class SplashActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {
	/**
	 * Main layout for this component.
	 */
	private static final int LAYOUT = R.layout.activity_splash;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		SystemUiHelper uiHelper = new SystemUiHelper(this, SystemUiHelper.LEVEL_IMMERSIVE, 0);
		uiHelper.hide();
		super.onCreate(savedInstanceState);
		DataBindingUtil.setContentView(this, LAYOUT);
		requirePermission();
	}

	private void goToHome() {
		CameraActivity.showInstance(this);
		finish();
	}

	private static final int RC_PERMISSIONS = 123;


	@SuppressLint("InlinedApi")
	@AfterPermissionGranted(RC_PERMISSIONS)
	private void requirePermission() {
		if (hasPermission()) {
			goToHome();
		} else {
			// Ask for one permission
			EasyPermissions.requestPermissions(this, getString(R.string.permission_relation_to_camera_text), RC_PERMISSIONS, CAMERA);
		}
	}


	@SuppressLint("InlinedApi")
	private boolean hasPermission() {
		return EasyPermissions.hasPermissions(this, CAMERA);
	}


	@Override
	public void onPermissionsDenied(int i, List<String> list) {
		if (!hasPermission()) {
			new AppSettingsDialog.Builder(this).setPositiveButton(R.string.permission_setting)
			                                   .setNegativeButton(getString(R.string.exit_app), new DialogInterface.OnClickListener() {
				                                   @Override
				                                   public void onClick(DialogInterface dialogInterface, int i) {
					                                   supportFinishAfterTransition();
				                                   }
			                                   })
			                                   .build()
			                                   .show();
		} else {
			goToHome();
		}
	}

	@Override
	public void onPermissionsGranted(int i, List<String> list) {
		goToHome();
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
	}
}
