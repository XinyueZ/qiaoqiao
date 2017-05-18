package com.qiaoqiao.ds;


import com.qiaoqiao.app.AppComponent;
import com.qiaoqiao.backend.BackendModule;
import com.qiaoqiao.ds.annotation.DsScope;
import com.qiaoqiao.keymanager.KeyManagerModule;

import dagger.Component;


@DsScope
@Component(dependencies = AppComponent.class, modules = { DsRepositoryModule.class,
                                                          BackendModule.class,
                                                          KeyManagerModule.class })
public interface DsRepositoryComponent {

	DsRepository getDsRepository();
}
