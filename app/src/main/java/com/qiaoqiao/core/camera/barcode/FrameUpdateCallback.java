package com.qiaoqiao.core.camera.barcode;

import android.hardware.Camera;

public interface FrameUpdateCallback {
	void onUpdated(byte[] data, Camera.Size previewSize, int previewFormat);
}
