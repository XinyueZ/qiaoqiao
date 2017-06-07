package com.qiaoqiao.core.splash

import com.qiaoqiao.app.AppComponent
import com.qiaoqiao.core.splash.annotation.SplashScoped
import com.qiaoqiao.core.splash.ui.SplashActivity
import com.qiaoqiao.repository.DsRepositoryComponent
import dagger.Component

@SplashScoped
@Component(dependencies = arrayOf(AppComponent::class, DsRepositoryComponent::class), modules = arrayOf(SplashModule::class))
interface SplashComponent {
    abstract fun injectSplashActivity(activity: SplashActivity)
}