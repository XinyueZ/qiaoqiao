package com.qiaoqiao.ds.local;


import android.support.annotation.NonNull;

import com.qiaoqiao.backend.Service;
import com.qiaoqiao.ds.AbstractDsSource;
import com.qiaoqiao.keymanager.Key;
import com.qiaoqiao.utils.LL;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

import javax.inject.Singleton;

import static com.qiaoqiao.ds.DsUtils.convertBytes;

@Singleton
public final class DsLocalSource extends AbstractDsSource {

	public DsLocalSource(@NonNull Service service, @NonNull Key key) {
		super(service, key);
	}

	@Override
	public void readLocal(@NonNull File file, BytesLoadedCallback callback) {
		try {
			byte bytes[] = FileUtils.readFileToByteArray(file);
			byte[] imageBytes = convertBytes(bytes);
			callback.onVisionResponse(null);
		} catch (IOException e) {
			LL.e(e.toString());
			callback.onError(e);
		}
	}
}
