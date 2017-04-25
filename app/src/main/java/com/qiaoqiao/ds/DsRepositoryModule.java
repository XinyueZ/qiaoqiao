package com.qiaoqiao.ds;

import android.support.annotation.NonNull;

import com.qiaoqiao.backend.Service;
import com.qiaoqiao.backend.WikipediaAPIs;
import com.qiaoqiao.ds.camera.DsCameraSource;
import com.qiaoqiao.ds.local.DsLocalSource;
import com.qiaoqiao.ds.web.DsWebSource;
import com.qiaoqiao.ds.wikipedia.DsWikipediaRemoteSource;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;


@Module
final class DsRepositoryModule {
	@Singleton
	@Web
	@Provides
	AbstractDsSource provideWebDataSource(@NonNull Service service) {
		return new DsWebSource(service);
	}


	@Singleton
	@Local
	@Provides
	AbstractDsSource provideLocalDataSource(@NonNull Service service) {
		return new DsLocalSource(service);
	}


	@Singleton
	@Camera
	@Provides
	AbstractDsSource provideCameraDataSource(@NonNull Service service) {
		return new DsCameraSource(service);
	}


	@Singleton
	@Wikipedia
	@Provides
	AbstractDsSource provideWikipediaDataSource(@NonNull WikipediaAPIs wikipediaAPIs) {
		return new DsWikipediaRemoteSource(wikipediaAPIs);
	}

	@Singleton
	@Provides
	DsRepository provideRepository(@NonNull Service service, @NonNull WikipediaAPIs wikipediaAPIs, @Web AbstractDsSource webDs, @Local AbstractDsSource localDs, @Camera AbstractDsSource cameraDs, @Wikipedia AbstractDsSource wikipediaRemoteDs) {
		return new DsRepository(service, wikipediaAPIs, webDs, localDs, cameraDs, wikipediaRemoteDs);
	}
}
