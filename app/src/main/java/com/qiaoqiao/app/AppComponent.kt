package com.qiaoqiao.app

import android.content.Context

import dagger.Component

@Component(modules = arrayOf(AppModule::class))
interface AppComponent {

    fun getContext(): Context

    fun getKey(): Key
}
