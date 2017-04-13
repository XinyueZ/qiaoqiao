package com.qiaoqiao.backend;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = BackendModule.class)
public interface BackendComponent {
	Service getService();
}
