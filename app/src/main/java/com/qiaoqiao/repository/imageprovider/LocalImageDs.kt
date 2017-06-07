package com.qiaoqiao.repository.imageprovider

import android.net.Uri
import com.qiaoqiao.repository.AbstractDsSource
import com.qiaoqiao.repository.DsLoadedCallback

class LocalImageDs : AbstractDsSource() {
    override fun onImage(callback: DsLoadedCallback) {
        callback.onImageLoad(Uri.parse("https://pic1.zhimg.com/v2-cf690e166adee2d77ebb3450d4ddc424.jpg"))
    }
}