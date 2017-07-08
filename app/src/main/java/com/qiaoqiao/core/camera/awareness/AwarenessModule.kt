package com.qiaoqiao.core.camera.awareness

import android.content.Context
import com.qiaoqiao.core.camera.annotation.CameraScoped
import com.qiaoqiao.core.camera.awareness.ui.SnapshotPlacesFragment
import dagger.Module
import dagger.Provides

@Module
class AwarenessModule {

    @Provides
    @CameraScoped
    fun provideAwarenessPlacesContractView(cxt: Context): AwarenessContract.View {
        return SnapshotPlacesFragment.newInstance(cxt)
    }

}