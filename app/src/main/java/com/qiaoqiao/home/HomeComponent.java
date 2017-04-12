package com.qiaoqiao.home;

import com.qiaoqiao.app.ActivityScoped;
import com.qiaoqiao.ds.DsRepositoryComponent;
import com.qiaoqiao.home.ui.HomeActivity;

import dagger.Component;

@ActivityScoped
@Component(dependencies = DsRepositoryComponent.class, modules = HomeModule.class)
public interface HomeComponent {
	void injectHome(HomeActivity activity);
}
