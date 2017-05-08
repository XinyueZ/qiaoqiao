package com.qiaoqiao.detail;


import com.qiaoqiao.app.ActivityScoped;
import com.qiaoqiao.detail.ui.DetailActivity;
import com.qiaoqiao.ds.DsRepositoryComponent;

import dagger.Component;

@ActivityScoped
@Component(dependencies = DsRepositoryComponent.class, modules = DetailModule.class)
public  interface DetailComponent {
	void injectDetail(DetailActivity activity);
}
