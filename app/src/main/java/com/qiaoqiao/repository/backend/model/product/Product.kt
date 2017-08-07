package com.qiaoqiao.repository.backend.model.product

import com.google.gson.annotations.SerializedName

data
class Product(@SerializedName("status") val status: Int,
              @SerializedName("product") val product: String,
              @SerializedName("description") val description: String,
              @SerializedName("barcodeSource") val barcodeSource: String,
              @SerializedName("company") val company: Company,
              @SerializedName("people") val people: String,
              @SerializedName("source") val source: String)