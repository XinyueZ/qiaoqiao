package com.qiaoqiao.ds.local;


import android.content.Context;
import android.support.annotation.NonNull;

import com.qiaoqiao.ds.DsSource;
import com.qiaoqiao.ds.DsType;

import javax.inject.Singleton;

@Singleton
public final class DsLocalSource implements DsSource {
	private final static @DsType int sDsType = DsType.LOCAL;

	@Override
	public void loadData(@NonNull Context cxt, @DsType int dsType, @NonNull    final DataLoadedCallback callback) {
		if (sDsType == dsType) {
			callback.onLoaded(null);
		}
	}
}
