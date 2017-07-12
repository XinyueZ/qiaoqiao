package com.qiaoqiao.core.camera.ui;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import com.afollestad.materialcamera.internal.CameraFragment;

public class Camera1Activity extends CameraActivity {

	@Override
	@NonNull
	public Fragment getFragment() {
		return CameraFragment.newInstance();
	}
}
