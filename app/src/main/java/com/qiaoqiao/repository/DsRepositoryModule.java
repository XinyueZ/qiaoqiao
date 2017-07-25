package com.qiaoqiao.repository;

import android.support.annotation.NonNull;

import com.qiaoqiao.app.Key;
import com.qiaoqiao.repository.annotation.RepositoryScope;
import com.qiaoqiao.repository.annotation.target.Camera;
import com.qiaoqiao.repository.annotation.target.Database;
import com.qiaoqiao.repository.annotation.target.Knowledge;
import com.qiaoqiao.repository.annotation.target.LocalImage;
import com.qiaoqiao.repository.annotation.target.RemoteImage;
import com.qiaoqiao.repository.backend.Google;
import com.qiaoqiao.repository.backend.ImageProvider;
import com.qiaoqiao.repository.backend.Wikipedia;
import com.qiaoqiao.repository.camera.DsCameraSource;
import com.qiaoqiao.repository.database.DsDatabaseSource;
import com.qiaoqiao.repository.imageprovider.LocalImageDs;
import com.qiaoqiao.repository.imageprovider.RemoteImageDs;
import com.qiaoqiao.repository.knowledge.DsKnowledgeRemoteSource;

import dagger.Module;
import dagger.Provides;


@Module
final class DsRepositoryModule {


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
	@RemoteImage
	@Provides
	AbstractDsSource provideRemoteImageDataSource(@NonNull ImageProvider imageProvider) {
		return new RemoteImageDs(imageProvider);
	}

	@RepositoryScope
	@LocalImage
	@Provides
	AbstractDsSource provideLocalImageDataSource(@NonNull ImageProvider imageProvider) {
		return new LocalImageDs(imageProvider);
	}


	@RepositoryScope
	@Provides
	DsRepository provideRepository(@NonNull Google google,
	                               @NonNull Wikipedia wikipedia,
	                               @Camera AbstractDsSource cameraDs,
	                               @Knowledge AbstractDsSource knowledgeRemoteDs,
	                               @Database AbstractDsSource databaseDs,
	                               @RemoteImage AbstractDsSource remoteImageDs,
	                               @LocalImage AbstractDsSource localImageDs) {
		return new DsRepository(google, wikipedia,  cameraDs, knowledgeRemoteDs, databaseDs, remoteImageDs, localImageDs);
	}
}
