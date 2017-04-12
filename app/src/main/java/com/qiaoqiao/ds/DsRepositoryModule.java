package com.qiaoqiao.ds;

import com.qiaoqiao.ds.camera.DsCameraSource;
import com.qiaoqiao.ds.local.DsLocalSource;
import com.qiaoqiao.ds.web.DsWebSource;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;


@Module
final class DsRepositoryModule {
	@Singleton
	@Web
	@Provides
	DsSource provideWebDataSource() {
		return new DsWebSource();
	}


	@Singleton
	@Local
	@Provides
	DsSource provideLocalDataSource() {
		return new DsLocalSource();
	}


	@Singleton
	@Camera
	@Provides
	DsSource provideCameraDataSource() {
		return new DsCameraSource();
	}

}
