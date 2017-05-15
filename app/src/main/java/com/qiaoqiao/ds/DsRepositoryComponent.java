package com.qiaoqiao.ds;


import com.qiaoqiao.app.AppModule;
import com.qiaoqiao.backend.BackendModule;
import com.qiaoqiao.ds.annotation.DsScope;
import com.qiaoqiao.keymanager.KeyManagerModule;

import dagger.Component;


@DsScope
@Component(modules = { DsRepositoryModule.class,
                       BackendModule.class,
                       KeyManagerModule.class,
                       AppModule.class} )
public interface DsRepositoryComponent {

	DsRepository getDsRepository();
}
