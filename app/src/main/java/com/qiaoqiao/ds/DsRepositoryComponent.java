package com.qiaoqiao.ds;


import com.qiaoqiao.app.AppModule;
import com.qiaoqiao.backend.BackendModule;
import com.qiaoqiao.keymanager.KeyManagerModule;

import javax.inject.Singleton;

import dagger.Component;


@Singleton
@Component(modules = { DsRepositoryModule.class,
                       BackendModule.class,
                       KeyManagerModule.class,
                       AppModule.class} )
public interface DsRepositoryComponent {

	DsRepository getDsRepository();
}
