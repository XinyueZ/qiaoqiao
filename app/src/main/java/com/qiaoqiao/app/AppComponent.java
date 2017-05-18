package com.qiaoqiao.app;


import android.content.Context;

import dagger.Component;


@Component(modules = { AppModule.class })
public interface AppComponent {

	Context getContext();
}
