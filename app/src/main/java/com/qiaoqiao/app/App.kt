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

package com.qiaoqiao.app

import android.support.customtabs.CustomTabsIntent
import android.support.multidex.MultiDexApplication
import android.support.v4.content.res.ResourcesCompat
import com.crashlytics.android.Crashlytics
import com.qiaoqiao.R
import com.qiaoqiao.core.camera.CameraModule
import com.qiaoqiao.core.camera.DaggerCameraComponent
import com.qiaoqiao.core.camera.awareness.AwarenessModule
import com.qiaoqiao.core.camera.crop.CropModule
import com.qiaoqiao.core.camera.history.HistoryModule
import com.qiaoqiao.core.camera.ui.CameraActivity
import com.qiaoqiao.core.camera.vision.VisionModule
import com.qiaoqiao.core.confidence.ConfidenceModule
import com.qiaoqiao.core.detail.DaggerDetailComponent
import com.qiaoqiao.core.detail.DetailModule
import com.qiaoqiao.core.detail.ui.DetailActivity
import com.qiaoqiao.core.product.DaggerProductComponent
import com.qiaoqiao.core.product.ProductModule
import com.qiaoqiao.core.product.ui.ProductListActivity
import com.qiaoqiao.core.splash.DaggerSplashComponent
import com.qiaoqiao.core.splash.SplashModule
import com.qiaoqiao.core.splash.ui.SplashActivity
import com.qiaoqiao.repository.DaggerDsRepositoryComponent
import com.qiaoqiao.repository.DsRepositoryComponent
import com.qiaoqiao.repository.backend.BackendModule
import de.immowelt.mobile.livestream.core.utils.customtab.CustomTabConfig
import io.fabric.sdk.android.Fabric
import io.realm.Realm
import io.realm.RealmConfiguration

class App : MultiDexApplication() {
    private var appComponent: AppComponent? = null
    private var repositoryComponent: DsRepositoryComponent? = null

    lateinit var customTabConfig: CustomTabConfig

    override fun onCreate() {
        super.onCreate()
        Fabric.with(this, Crashlytics())
        Realm.init(applicationContext)
        customTabConfig = createCustomTabConfig()

        appComponent = DaggerAppComponent.builder()
                .appModule(AppModule(this))
                .build()
        repositoryComponent = DaggerDsRepositoryComponent.builder()
                .appComponent(appComponent)
                .backendModule(BackendModule())
                .build()
    }

    private fun createCustomTabConfig(): CustomTabConfig {
        return CustomTabConfig(
                CustomTabsIntent.Builder()
                        .setShowTitle(true)
                        .setToolbarColor(ResourcesCompat.getColor(resources, R.color.colorPrimary, null)))
    }


    companion object {

        val realm: Realm
            get() = Realm.getInstance(RealmConfiguration.Builder().deleteRealmIfMigrationNeeded()
                    .build())

        fun inject(cameraActivity: CameraActivity) {
            val application = cameraActivity.application as App
            DaggerCameraComponent.builder()
                    .appComponent(application.appComponent)
                    .dsRepositoryComponent(application.repositoryComponent)
                    .cameraModule(CameraModule(cameraActivity))
                    .historyModule(HistoryModule(cameraActivity.supportFragmentManager))
                    .visionModule(VisionModule())
                    .awarenessModule(AwarenessModule())
                    .cropModule(CropModule())
                    .confidenceModule(ConfidenceModule())
                    .build()
                    .doInject(cameraActivity)
        }

        fun inject(detailActivity: DetailActivity) {
            val application = detailActivity.application as App
            DaggerDetailComponent.builder()
                    .dsRepositoryComponent(application.repositoryComponent)
                    .detailModule(DetailModule(detailActivity.supportFragmentManager))
                    .build()
                    .injectDetail(detailActivity)
        }

        fun inject(splashActivity: SplashActivity) {
            val application = splashActivity.application as App
            DaggerSplashComponent.builder()
                    .dsRepositoryComponent(application.repositoryComponent)
                    .appComponent(application.appComponent)
                    .splashModule(SplashModule())
                    .build()
                    .injectSplashActivity(splashActivity)
        }

        fun inject(productListActivity: ProductListActivity) {
            val application = productListActivity.application as App
            DaggerProductComponent.builder()
                    .appComponent(application.appComponent)
                    .dsRepositoryComponent(application.repositoryComponent)
                    .productModule(ProductModule())
                    .build()
                    .injectProductList(productListActivity)
        }
    }
}
