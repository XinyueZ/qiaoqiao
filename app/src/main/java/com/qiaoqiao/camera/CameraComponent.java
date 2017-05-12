package com.qiaoqiao.camera;

import com.qiaoqiao.app.ActivityScoped;
import com.qiaoqiao.ds.DsRepositoryComponent;
import com.qiaoqiao.camera.ui.CameraActivity;

import dagger.Component;

@ActivityScoped
@Component(dependencies = DsRepositoryComponent.class, modules = CameraModule.class)
public interface CameraComponent {
	void injectCamera(CameraActivity activity);
}
