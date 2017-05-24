package com.qiaoqiao.core.camera.crop;


import android.support.annotation.NonNull;

public interface CropCallback {
	void onCropped(@NonNull byte[] bytes);

	void onCroppedFail();
}
