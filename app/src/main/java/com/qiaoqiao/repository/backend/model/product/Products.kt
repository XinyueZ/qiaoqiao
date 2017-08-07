package com.qiaoqiao.repository.backend.model.product

import com.google.gson.annotations.SerializedName

data class Products(@SerializedName("result") val result: List<Product>)