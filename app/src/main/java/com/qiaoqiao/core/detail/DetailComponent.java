package com.qiaoqiao.core.detail;


import com.qiaoqiao.core.detail.annotation.DetailScoped;
import com.qiaoqiao.core.detail.ui.DetailActivity;
import com.qiaoqiao.repository.DsRepositoryComponent;

import dagger.Component;

@DetailScoped
@Component(dependencies = DsRepositoryComponent.class, modules = DetailModule.class)
public interface DetailComponent {
	void injectDetail(DetailActivity activity);
}
