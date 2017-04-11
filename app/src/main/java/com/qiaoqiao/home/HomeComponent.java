package com.qiaoqiao.home;

import android.support.annotation.NonNull;

import com.qiaoqiao.app.ActivityScoped;
import com.qiaoqiao.app.AppComponent;

import dagger.Component;

@ActivityScoped
@Component(dependencies = AppComponent.class, modules = HomeModule.class)
public interface HomeComponent {
	void inject(@NonNull HomeActivity homeActivity);
}
