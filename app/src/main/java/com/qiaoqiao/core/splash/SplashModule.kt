package com.qiaoqiao.core.splash

import android.content.Context
import com.qiaoqiao.core.splash.ui.LaunchImageFragment
import dagger.Module
import dagger.Provides

@Module
class SplashModule {
    @Provides
    fun getSplashContractView(cxt: Context): SplashContract.View = LaunchImageFragment.newInstance(cxt)
}