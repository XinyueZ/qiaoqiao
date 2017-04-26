package com.qiaoqiao.ds;

import android.support.annotation.NonNull;

import com.qiaoqiao.backend.Google;
import com.qiaoqiao.backend.Wikipedia;
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
	AbstractDsSource provideWebDataSource(@NonNull Google google) {
		return new DsWebSource(google);
	}


	@Singleton
	@Local
	@Provides
	AbstractDsSource provideLocalDataSource(@NonNull Google google) {
		return new DsLocalSource(google);
	}


	@Singleton
	@Camera
	@Provides
	AbstractDsSource provideCameraDataSource(@NonNull Google google) {
		return new DsCameraSource(google);
	}


	@Singleton
	@Knowledge
	@Provides
	AbstractDsSource provideWikipediaDataSource(@NonNull com.qiaoqiao.backend.Wikipedia wikipedia) {
		return new DsWikipediaRemoteSource(wikipedia);
	}

	@Singleton
	@Provides
	DsRepository provideRepository(@NonNull Google google, @NonNull Wikipedia wikipedia, @Web AbstractDsSource webDs, @Local AbstractDsSource localDs, @Camera AbstractDsSource cameraDs, @Knowledge AbstractDsSource wikipediaRemoteDs) {
		return new DsRepository(google, wikipedia, webDs, localDs, cameraDs, wikipediaRemoteDs);
	}
}
