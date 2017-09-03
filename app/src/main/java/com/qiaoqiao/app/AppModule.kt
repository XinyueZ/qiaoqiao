package com.qiaoqiao.app

import android.content.Context
import dagger.Module
import dagger.Provides
import java.util.*

@Module
class AppModule internal constructor(private val mApp: App) {

    @Provides
    internal fun provideApp(): Context {
        return mApp
    }

    @Provides
    internal fun provideKey(cxt: Context): Key {
        with(Properties()) {
            load(cxt.classLoader.getResourceAsStream("key.properties"))
            val key = getProperty("apikey")
            key?.let {
                return Key(it)
            }
        }
        throw NullPointerException("The Api-key can't be found in file key.properties .")
    }
}
