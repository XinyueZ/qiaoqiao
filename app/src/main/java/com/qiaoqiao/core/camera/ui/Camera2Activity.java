package com.qiaoqiao.core.camera.ui;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import com.afollestad.materialcamera.internal.Camera2Fragment;

public class Camera2Activity extends CameraActivity {

	@Override
	@NonNull
	public Fragment getFragment() {
		return Camera2Fragment.newInstance();
	}
}
