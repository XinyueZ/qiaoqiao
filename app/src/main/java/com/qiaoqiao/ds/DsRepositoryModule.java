package com.qiaoqiao.ds;

import android.support.annotation.NonNull;

import com.qiaoqiao.backend.Google;
import com.qiaoqiao.backend.Wikipedia;
import com.qiaoqiao.ds.annotation.Camera;
import com.qiaoqiao.ds.annotation.Database;
import com.qiaoqiao.ds.annotation.Knowledge;
import com.qiaoqiao.ds.annotation.Local;
import com.qiaoqiao.ds.annotation.Web;
import com.qiaoqiao.ds.camera.DsCameraSource;
import com.qiaoqiao.ds.database.DsDatabaseSource;
import com.qiaoqiao.ds.knowledge.DsKnowledgeRemoteSource;
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
	AbstractDsSource provideWikipediaDataSource(@NonNull Key key, @NonNull Google google, @NonNull com.qiaoqiao.backend.Wikipedia wikipedia) {
		return new DsKnowledgeRemoteSource(key, google, wikipedia);
	}


	@Singleton
	@Database
	@Provides
	AbstractDsSource provideDatabaseDataSource() {
		return new DsDatabaseSource();
	}

	@Singleton
	@Provides
	DsRepository provideRepository(@NonNull Google google,
	                               @NonNull Wikipedia wikipedia,
	                               @Web AbstractDsSource webDs,
	                               @Local AbstractDsSource localDs,
	                               @Camera AbstractDsSource cameraDs,
	                               @Knowledge AbstractDsSource knowledgeRemoteDs,
	                               @Database AbstractDsSource databaseDs) {
		return new DsRepository(google, wikipedia, webDs, localDs, cameraDs, knowledgeRemoteDs, databaseDs);
	}
}
