package com.qiaoqiao.core.camera.crop;


import android.support.annotation.NonNull;

import com.qiaoqiao.core.camera.crop.model.CropSource;

public interface CropCallback {
	void onCropped(@NonNull byte[] bytes);

	void onCroppedFail();

	void openCrop(@NonNull CropSource cropSource);
}
