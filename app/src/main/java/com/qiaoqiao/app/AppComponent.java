package com.qiaoqiao.app;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = ApplicationModule.class)
public interface AppComponent {
	App injectApp();
}
