package com.qiaoqiao.detail;


import com.qiaoqiao.detail.annotation.DetailScoped;
import com.qiaoqiao.detail.ui.DetailActivity;
import com.qiaoqiao.ds.DsRepositoryComponent;

import dagger.Component;

@DetailScoped
@Component(dependencies = DsRepositoryComponent.class, modules = DetailModule.class)
public  interface DetailComponent {
	void injectDetail(DetailActivity activity);
}
