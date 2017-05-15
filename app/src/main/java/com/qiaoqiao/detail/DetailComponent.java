package com.qiaoqiao.detail;


import com.qiaoqiao.camera.annotation.CameraScoped;
import com.qiaoqiao.detail.ui.DetailActivity;
import com.qiaoqiao.ds.DsRepositoryComponent;

import dagger.Component;

@CameraScoped
@Component(dependencies = DsRepositoryComponent.class, modules = DetailModule.class)
public  interface DetailComponent {
	void injectDetail(DetailActivity activity);
}
