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

import android.support.multidex.MultiDexApplication;

import com.google.gson.Gson;
import com.qiaoqiao.backend.BackendModule;
import com.qiaoqiao.ds.DaggerDsRepositoryComponent;
import com.qiaoqiao.ds.DsRepositoryComponent;
import com.qiaoqiao.keymanager.KeyManagerModule;


public final class App extends MultiDexApplication {
	public DsRepositoryComponent mRepositoryComponent;

	private final Gson gson = new Gson();


	@Override
	public void onCreate() {
		super.onCreate();
		mRepositoryComponent = DaggerDsRepositoryComponent.builder()
		                                                  .backendModule(new BackendModule())
		                                                  .keyManagerModule(new KeyManagerModule())
		                                                  .appModule(new AppModule(this))
		                                                  .build();
	}

	public DsRepositoryComponent getRepositoryComponent() {
		return mRepositoryComponent;
	}


	public Gson getGson() {
		return gson;
	}
}
