package com.qiaoqiao.repository;


import com.qiaoqiao.app.AppComponent;
import com.qiaoqiao.repository.annotation.RepositoryScope;
import com.qiaoqiao.repository.backend.BackendModule;

import dagger.Component;


@RepositoryScope
@Component(dependencies = AppComponent.class, modules = { DsRepositoryModule.class,
                                                          BackendModule.class})
public interface DsRepositoryComponent {

	DsRepository getDsRepository();
}
