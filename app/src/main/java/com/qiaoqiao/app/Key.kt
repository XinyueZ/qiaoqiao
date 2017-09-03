package com.qiaoqiao.app

import java.io.Serializable

class Key internal constructor(private val mApiKey: String) : Serializable {

    override fun toString(): String {
        return mApiKey
    }
}