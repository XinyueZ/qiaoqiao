package com.qiaoqiao.home;

import android.support.annotation.NonNull;

import com.qiaoqiao.app.ActivityScoped;
import com.qiaoqiao.home.ui.HomeActivity;

import dagger.Component;

@ActivityScoped
@Component(modules = HomeModule.class)
public interface HomeComponent {
	void injectHome(@NonNull HomeActivity homeActivity);
}
