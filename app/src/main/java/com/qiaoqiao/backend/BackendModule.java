package com.qiaoqiao.backend;

import android.content.Context;
import android.support.annotation.NonNull;

import com.qiaoqiao.keymanager.Key;

import dagger.Module;
import dagger.Provides;


@Module
public final class BackendModule {

	@Provides
	Service provideService(@NonNull Context cxt, @NonNull Key key) {
		return new Service(cxt, key);
	}

}
