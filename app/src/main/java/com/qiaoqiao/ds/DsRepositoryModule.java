package com.qiaoqiao.ds;

import android.support.annotation.NonNull;

import com.qiaoqiao.backend.Service;
import com.qiaoqiao.ds.camera.DsCameraSource;
import com.qiaoqiao.ds.local.DsLocalSource;
import com.qiaoqiao.ds.web.DsWebSource;
import com.qiaoqiao.keymanager.Key;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;


@Module
final class DsRepositoryModule {
	@Singleton
	@Web
	@Provides
	AbstractDsSource provideWebDataSource(@NonNull Service service, @NonNull Key key) {
		return new DsWebSource(service, key);
	}


	@Singleton
	@Local
	@Provides
	AbstractDsSource provideLocalDataSource(@NonNull Service service, @NonNull Key key) {
		return new DsLocalSource(service, key);
	}


	@Singleton
	@Camera
	@Provides
	AbstractDsSource provideCameraDataSource(@NonNull Service service, @NonNull Key key) {
		return new DsCameraSource(service, key);
	}

	@Singleton
	@Provides
	DsRepository provideRepository(@NonNull Service service, @NonNull Key key, @Web AbstractDsSource webDs, @Local AbstractDsSource localDs, @Camera AbstractDsSource cameraDs) {
		return new DsRepository(service, key, webDs, localDs, cameraDs);
	}
}
