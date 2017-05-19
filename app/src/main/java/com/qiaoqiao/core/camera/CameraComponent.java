package com.qiaoqiao.core.camera;

import com.qiaoqiao.app.AppComponent;
import com.qiaoqiao.core.camera.awareness.AwarenessModule;
import com.qiaoqiao.core.camera.annotation.CameraScoped;
import com.qiaoqiao.core.camera.ui.CameraActivity;
import com.qiaoqiao.repository.DsRepositoryComponent;
import com.qiaoqiao.core.camera.history.HistoryModule;
import com.qiaoqiao.core.camera.vision.VisionModule;

import dagger.Component;

@CameraScoped
@Component(dependencies = { AppComponent.class, DsRepositoryComponent.class}  , modules = { CameraModule.class,
                                                                        VisionModule.class,
                                                                        HistoryModule.class,
                                                                        AwarenessModule.class })
public interface CameraComponent {
	void doInject(CameraActivity activity);
}
