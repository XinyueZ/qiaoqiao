package com.qiaoqiao.ds;

import android.support.annotation.NonNull;

import com.qiaoqiao.backend.Google;
import com.qiaoqiao.backend.Wikipedia;
import com.qiaoqiao.ds.annotation.target.Camera;
import com.qiaoqiao.ds.annotation.target.Database;
import com.qiaoqiao.ds.annotation.DsScope;
import com.qiaoqiao.ds.annotation.target.Knowledge;
import com.qiaoqiao.ds.annotation.target.Local;
import com.qiaoqiao.ds.annotation.target.Web;
import com.qiaoqiao.ds.camera.DsCameraSource;
import com.qiaoqiao.ds.database.DsDatabaseSource;
import com.qiaoqiao.ds.knowledge.DsKnowledgeRemoteSource;
import com.qiaoqiao.ds.local.DsLocalSource;
import com.qiaoqiao.ds.web.DsWebSource;
import com.qiaoqiao.keymanager.Key;

import dagger.Module;
import dagger.Provides;


@Module
final class DsRepositoryModule {
	@DsScope
	@Web
	@Provides
	AbstractDsSource provideWebDataSource(@NonNull Google google) {
		return new DsWebSource(google);
	}


	@DsScope
	@Local
	@Provides
	AbstractDsSource provideLocalDataSource(@NonNull Google google) {
		return new DsLocalSource(google);
	}


	@DsScope
	@Camera
	@Provides
	AbstractDsSource provideCameraDataSource(@NonNull Google google) {
		return new DsCameraSource(google);
	}


	@DsScope
	@Knowledge
	@Provides
	AbstractDsSource provideWikipediaDataSource(@NonNull Key key, @NonNull Google google, @NonNull com.qiaoqiao.backend.Wikipedia wikipedia) {
		return new DsKnowledgeRemoteSource(key, google, wikipedia);
	}


	@DsScope
	@Database
	@Provides
	AbstractDsSource provideDatabaseDataSource() {
		return new DsDatabaseSource();
	}

	@DsScope
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
