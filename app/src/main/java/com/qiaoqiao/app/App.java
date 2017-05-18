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

import com.qiaoqiao.awareness.AwarenessModule;
import com.qiaoqiao.backend.BackendModule;
import com.qiaoqiao.camera.CameraModule;
import com.qiaoqiao.camera.DaggerCameraComponent;
import com.qiaoqiao.camera.ui.CameraActivity;
import com.qiaoqiao.detail.DaggerDetailComponent;
import com.qiaoqiao.detail.DetailModule;
import com.qiaoqiao.detail.ui.DetailActivity;
import com.qiaoqiao.ds.DaggerDsRepositoryComponent;
import com.qiaoqiao.ds.DsRepositoryComponent;
import com.qiaoqiao.history.HistoryModule;
import com.qiaoqiao.keymanager.KeyManagerModule;
import com.qiaoqiao.vision.VisionModule;

import io.realm.Realm;


public final class App extends MultiDexApplication {
	private AppComponent mAppComponent;
	private DsRepositoryComponent mRepositoryComponent;


	@Override
	public void onCreate() {
		super.onCreate();
		Realm.init(getApplicationContext());
		mAppComponent = DaggerAppComponent.builder()
		                                  .appModule(new AppModule(this))
		                                  .build();
		mRepositoryComponent = DaggerDsRepositoryComponent.builder()
		                                                  .appComponent(mAppComponent)
		                                                  .backendModule(new BackendModule())
		                                                  .keyManagerModule(new KeyManagerModule())
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
		                     .build()
		                     .doInject(cameraActivity);
	}

	public static void inject(@NonNull DetailActivity detailActivity, @NonNull String keyword) {
		final App application = (App) detailActivity.getApplication();
		DaggerDetailComponent.builder()
		                     .dsRepositoryComponent(application.mRepositoryComponent)
		                     .detailModule(new DetailModule(detailActivity.getSupportFragmentManager(), keyword))
		                     .build()
		                     .injectDetail(detailActivity);
	}

}
