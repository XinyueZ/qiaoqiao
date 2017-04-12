package com.qiaoqiao.ds;


import android.content.Context;
import android.support.annotation.NonNull;

public interface DsSource {
	void loadData(@NonNull Context cxt, @DsType int dsType, @NonNull DataLoadedCallback callback);

	interface DataLoadedCallback {
		void on();
		void onLoaded(@NonNull final byte[] data);
	}
}
