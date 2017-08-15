package com.qiaoqiao.repository.backend.model.product

import com.google.gson.annotations.SerializedName

data class ProductImage(@SerializedName("small") val small: List<String>,
                   @SerializedName("medium") val medium: List<String>,
                   @SerializedName("large") val large: List<String>,
                   @SerializedName("thumbnail") val thumbnail: String,
                   @SerializedName("brand") val brand: String)