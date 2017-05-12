package com.qiaoqiao.vision;

import com.qiaoqiao.app.ActivityScoped;
import com.qiaoqiao.ds.DsRepositoryComponent;

import dagger.Component;

@ActivityScoped
@Component(dependencies = DsRepositoryComponent.class, modules = VisionModule.class)
public interface VisionComponent {
	VisionPresenter getVisionManager();
}
