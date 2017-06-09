package com.qiaoqiao.core.splash.ui

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import com.qiaoqiao.R
import com.qiaoqiao.app.App
import com.qiaoqiao.core.splash.SplashContract
import com.qiaoqiao.core.splash.SplashPresenter
import com.qiaoqiao.databinding.ActivitySplashBinding
import com.qiaoqiao.utils.SystemUiHelper
import java.util.concurrent.TimeUnit
import javax.inject.Inject

const val LAYOUT: Int = R.layout.activity_splash

class SplashActivity : AppCompatActivity() {
    private var binding: ActivitySplashBinding? = null
    @Inject private var presenter: SplashPresenter? = null
    @Inject private var launchImageView: SplashContract.LaunchImageView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val uiHelper = SystemUiHelper(this, SystemUiHelper.LEVEL_IMMERSIVE, 0)
        uiHelper.hide()
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView<ActivitySplashBinding>(this, LAYOUT)
        binding?.uiHelper = uiHelper
        App.inject(this)
    }

    @Inject
    fun injected() {
        Handler().postDelayed({ -> gotoLaunchImage() }, TimeUnit.SECONDS.toMillis(5))
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        binding?.uiHelper?.hide()
        super.onWindowFocusChanged(hasFocus)
    }

    private fun gotoLaunchImage() {
        supportFragmentManager.beginTransaction()
                .replace(R.id.splash_root_fl, launchImageView as Fragment?)
                .commit()

        presenter?.begin(this)
    }

    override fun onDestroy() {
        presenter?.end(this)
        super.onDestroy()
    }
}