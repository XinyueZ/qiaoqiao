package com.qiaoqiao.ds;


import javax.inject.Singleton;

import dagger.Component;


@Singleton
@Component(modules = DsRepositoryModule.class)
public interface DsRepositoryComponent {

	DsRepository getDsRepository();


}
