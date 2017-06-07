/*
                   _ooOoo_
                  o8888888o
                  88" . "88
                  (| -_- |)
                  O\  =  /O
               ____/`---'\____
             .'  \\|     |//  `.
            /  \\|||  :  |||//  \
           /  _||||| -:- |||||-  \
           |   | \\\  -  /// |   |
           | \_|  ''\---/''  |   |
           \  .-\__  `-`  ___/-. /
         ___`. .'  /--.--\  `. . __
      ."" '<  `.___\_<|>_/___.'  >'"".
     | | :  `- \`.;`\ _ /`;.`/ - ` : | |
     \  \ `-.   \_ __\ /__ _/   .-` /  /
======`-.____`-.___\_____/___.-`____.-'======
                   `=---='
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
         佛祖保佑       永无BUG
*/
//          佛曰:
//                  写字楼里写字间，写字间里程序员；
//                  程序人员写程序，又拿程序换酒钱。
//                  酒醒只在网上坐，酒醉还来网下眠；
//                  酒醉酒醒日复日，网上网下年复年。
//                  但愿老死电脑间，不愿鞠躬老板前；
//                  奔驰宝马贵者趣，公交自行程序员。
//                  别人笑我忒疯癫，我笑自己命太贱。

package com.qiaoqiao.app;

import android.support.annotation.NonNull;
import android.support.multidex.MultiDexApplication;

import com.crashlytics.android.Crashlytics;
import com.qiaoqiao.core.camera.CameraModule;
import com.qiaoqiao.core.camera.DaggerCameraComponent;
import com.qiaoqiao.core.camera.awareness.AwarenessModule;
import com.qiaoqiao.core.camera.crop.CropModule;
import com.qiaoqiao.core.camera.history.HistoryModule;
import com.qiaoqiao.core.camera.ui.CameraActivity;
import com.qiaoqiao.core.camera.vision.VisionModule;
import com.qiaoqiao.core.confidence.ConfidenceModule;
import com.qiaoqiao.core.detail.DaggerDetailComponent;
import com.qiaoqiao.core.detail.DetailModule;
import com.qiaoqiao.core.detail.ui.DetailActivity;
import com.qiaoqiao.core.splash.DaggerSplashComponent;
import com.qiaoqiao.core.splash.SplashModule;
import com.qiaoqiao.core.splash.ui.SplashActivity;
import com.qiaoqiao.repository.DaggerDsRepositoryComponent;
import com.qiaoqiao.repository.DsRepositoryComponent;
import com.qiaoqiao.repository.backend.BackendModule;

import io.fabric.sdk.android.Fabric;
import io.realm.Realm;


public final class App extends MultiDexApplication {
	private AppComponent mAppComponent;
	private DsRepositoryComponent mRepositoryComponent;


	@Override
	public void onCreate() {
		super.onCreate();
		Fabric.with(this, new Crashlytics());
		Realm.init(getApplicationContext());
		mAppComponent = DaggerAppComponent.builder()
		                                  .appModule(new AppModule(this))
		                                  .build();
		mRepositoryComponent = DaggerDsRepositoryComponent.builder()
		                                                  .appComponent(mAppComponent)
		                                                  .backendModule(new BackendModule())
		                                                  .build();
	}


	public static void inject(@NonNull CameraActivity cameraActivity) {
		final App application = (App) cameraActivity.getApplication();
		DaggerCameraComponent.builder()
		                     .appComponent(application.mAppComponent)
		                     .dsRepositoryComponent(application.mRepositoryComponent)
		                     .cameraModule(new CameraModule(cameraActivity))
		                     .historyModule(new HistoryModule())
		                     .visionModule(new VisionModule())
		                     .awarenessModule(new AwarenessModule())
		                     .cropModule(new CropModule())
		                     .confidenceModule(new ConfidenceModule())
		                     .build()
		                     .doInject(cameraActivity);
	}

	public static void inject(@NonNull DetailActivity detailActivity) {
		final App application = (App) detailActivity.getApplication();
		DaggerDetailComponent.builder()
		                     .dsRepositoryComponent(application.mRepositoryComponent)
		                     .detailModule(new DetailModule(detailActivity.getSupportFragmentManager()))
		                     .build()
		                     .injectDetail(detailActivity);
	}


	public static void inject(@NonNull SplashActivity splashActivity) {
		final App application = (App) splashActivity.getApplication();
		DaggerSplashComponent.builder()
		                     .dsRepositoryComponent(application.mRepositoryComponent)
		                     .splashModule(new SplashModule())
		                     .build()
		                     .injectSplashActivity(splashActivity);
	}

}
