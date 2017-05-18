package com.qiaoqiao.camera;

import com.qiaoqiao.app.AppComponent;
import com.qiaoqiao.awareness.AwarenessModule;
import com.qiaoqiao.camera.annotation.CameraScoped;
import com.qiaoqiao.camera.ui.CameraActivity;
import com.qiaoqiao.ds.DsRepositoryComponent;
import com.qiaoqiao.history.HistoryModule;
import com.qiaoqiao.vision.VisionModule;

import dagger.Component;

@CameraScoped
@Component(dependencies = { AppComponent.class, DsRepositoryComponent.class}  , modules = { CameraModule.class,
                                                                        VisionModule.class,
                                                                        HistoryModule.class,
                                                                        AwarenessModule.class })
public interface CameraComponent {
	void doInject(CameraActivity activity);
}
