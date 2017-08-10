package com.qiaoqiao.repository.backend.model.product

import com.google.gson.annotations.SerializedName

class ProductImage(@SerializedName("url") val url: List<String>,
                   @SerializedName("thumbnail") val thumbnail: String,
                   @SerializedName("brand") val brand: String)