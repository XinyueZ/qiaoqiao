package com.qiaoqiao.repository;

import android.support.annotation.NonNull;

import com.qiaoqiao.repository.backend.Google;
import com.qiaoqiao.repository.backend.Wikipedia;
import com.qiaoqiao.repository.annotation.target.Camera;
import com.qiaoqiao.repository.annotation.target.Database;
import com.qiaoqiao.repository.annotation.RepositoryScope;
import com.qiaoqiao.repository.annotation.target.Knowledge;
import com.qiaoqiao.repository.annotation.target.Local;
import com.qiaoqiao.repository.annotation.target.Web;
import com.qiaoqiao.repository.camera.DsCameraSource;
import com.qiaoqiao.repository.database.DsDatabaseSource;
import com.qiaoqiao.repository.knowledge.DsKnowledgeRemoteSource;
import com.qiaoqiao.repository.local.DsLocalSource;
import com.qiaoqiao.repository.web.DsWebSource;
import com.qiaoqiao.app.Key;

import dagger.Module;
import dagger.Provides;


@Module
final class DsRepositoryModule {
	@RepositoryScope
	@Web
	@Provides
	AbstractDsSource provideWebDataSource(@NonNull Google google) {
		return new DsWebSource(google);
	}


	@RepositoryScope
	@Local
	@Provides
	AbstractDsSource provideLocalDataSource(@NonNull Google google) {
		return new DsLocalSource(google);
	}


	@RepositoryScope
	@Camera
	@Provides
	AbstractDsSource provideCameraDataSource(@NonNull Google google) {
		return new DsCameraSource(google);
	}


	@RepositoryScope
	@Knowledge
	@Provides
	AbstractDsSource provideWikipediaDataSource(@NonNull Key key, @NonNull Google google, @NonNull com.qiaoqiao.repository.backend.Wikipedia wikipedia) {
		return new DsKnowledgeRemoteSource(key, google, wikipedia);
	}


	@RepositoryScope
	@Database
	@Provides
	AbstractDsSource provideDatabaseDataSource() {
		return new DsDatabaseSource();
	}

	@RepositoryScope
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
