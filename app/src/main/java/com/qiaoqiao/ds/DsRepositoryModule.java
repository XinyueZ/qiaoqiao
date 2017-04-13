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
	AbstractDsSource provideWebDataSource() {
		return new DsWebSource();
	}


	@Singleton
	@Local
	@Provides
	AbstractDsSource provideLocalDataSource() {
		return new DsLocalSource();
	}


	@Singleton
	@Camera
	@Provides
	AbstractDsSource provideCameraDataSource() {
		return new DsCameraSource();
	}

}
