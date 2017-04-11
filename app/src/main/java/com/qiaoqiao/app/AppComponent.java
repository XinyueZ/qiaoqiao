package com.qiaoqiao.app;

import android.support.annotation.NonNull;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = ApplicationModule.class)
public interface AppComponent {
	void injectApp(@NonNull App app);
}
