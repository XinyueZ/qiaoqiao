package com.qiaoqiao.repository.imageprovider

import android.content.Context
import com.qiaoqiao.repository.AbstractDsSource
import com.qiaoqiao.repository.DsLoadedCallback
import com.qiaoqiao.repository.backend.ImageProvider
import com.qiaoqiao.repository.database.LastLaunchImage
import io.realm.Realm
import io.realm.RealmChangeListener
import io.realm.RealmResults

class LocalImageDs(imageProvider: ImageProvider) : AbstractDsSource(imageProvider) {
    override fun onImage(cxt: Context, callback: DsLoadedCallback) {
        Realm.getDefaultInstance()
                .where(LastLaunchImage::class.java)
                .findAllAsync()
                .addChangeListener(object : RealmChangeListener<RealmResults<LastLaunchImage>> {
                    override fun onChange(element: RealmResults<LastLaunchImage>) {
                        if (element.isLoaded && element.size > 0) {
                            element.removeChangeListener(this)
                            callback.onImageLoad(element[0]
                                    .byteArray)
                        }
                    }
                })
    }
}