package com.qiaoqiao.core.camera;

import com.qiaoqiao.app.AppComponent;
import com.qiaoqiao.core.camera.annotation.CameraScoped;
import com.qiaoqiao.core.camera.awareness.AwarenessModule;
import com.qiaoqiao.core.camera.crop.CropModule;
import com.qiaoqiao.core.camera.history.HistoryModule;
import com.qiaoqiao.core.camera.ui.CameraActivity;
import com.qiaoqiao.core.camera.vision.VisionModule;
import com.qiaoqiao.core.confidence.ConfidenceModule;
import com.qiaoqiao.repository.DsRepositoryComponent;

import dagger.Component;

@CameraScoped
@Component(dependencies = { AppComponent.class,
                            DsRepositoryComponent.class }, modules = { CropModule.class,
                                                                       CameraModule.class,
                                                                       VisionModule.class,
                                                                       HistoryModule.class,
                                                                       AwarenessModule.class,
                                                                       ConfidenceModule.class })
public interface CameraComponent {
	void doInject(CameraActivity activity);
}
