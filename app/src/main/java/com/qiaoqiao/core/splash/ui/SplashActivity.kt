package com.qiaoqiao.core.splash.ui

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import com.qiaoqiao.R
import com.qiaoqiao.app.App
import com.qiaoqiao.app.COMMON_DELAY_SEC
import com.qiaoqiao.core.splash.SplashContract
import com.qiaoqiao.core.splash.SplashPresenter
import com.qiaoqiao.databinding.SplashBinding
import com.qiaoqiao.utils.SystemUiHelper
import java.util.concurrent.TimeUnit
import javax.inject.Inject

private const val LAYOUT: Int = R.layout.activity_splash

class SplashActivity : AppCompatActivity() {
    lateinit private var binding: SplashBinding
    lateinit private var uiHelper: SystemUiHelper
    @Inject
    lateinit var presenter: SplashPresenter
    @Inject
    lateinit var launchImageView: SplashContract.LaunchImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        uiHelper = SystemUiHelper(this, SystemUiHelper.LEVEL_IMMERSIVE, 0)
        uiHelper.hide()
        super.onCreate(savedInstanceState)
        this.binding = DataBindingUtil.setContentView<SplashBinding>(this, LAYOUT)
        App.inject(this)
    }

    @Inject
    fun injected() {
        Handler().postDelayed({ -> gotoLaunchImage() }, TimeUnit.SECONDS.toMillis(COMMON_DELAY_SEC))
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        uiHelper.hide()
        super.onWindowFocusChanged(hasFocus)
    }

    private fun gotoLaunchImage() {
        supportFragmentManager.beginTransaction()
                .replace(R.id.splash_root_fl, launchImageView as Fragment?)
                .commit()

        presenter.begin(this)
    }

    override fun onDestroy() {
        presenter.end(this)
        super.onDestroy()
    }
}