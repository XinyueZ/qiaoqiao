package com.qiaoqiao.camera;

import com.qiaoqiao.app.ActivityScoped;
import com.qiaoqiao.camera.ui.CameraActivity;
import com.qiaoqiao.ds.DsRepositoryComponent;
import com.qiaoqiao.history.HistoryModule;
import com.qiaoqiao.vision.VisionModule;

import dagger.Component;

@ActivityScoped
@Component(dependencies = DsRepositoryComponent.class, modules = { CameraModule.class,
                                                                   VisionModule.class,
                                                                   HistoryModule.class})
public interface CameraComponent {
	void doInject(CameraActivity activity);
}
