package com.qiaoqiao.repository.backend.model.product

import com.google.gson.annotations.SerializedName

data class Company(
        @SerializedName("name") val name: String,
        @SerializedName("logo") val logo: String
)