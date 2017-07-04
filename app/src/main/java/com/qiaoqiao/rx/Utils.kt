package com.qiaoqiao.rx



fun <T> safeList(list: MutableList<T>?)  = list ?: arrayListOf<T>()