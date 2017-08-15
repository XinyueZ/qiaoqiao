package com.qiaoqiao.core.product.model

import android.net.Uri
import android.text.TextUtils
import com.qiaoqiao.repository.backend.model.product.Product
import io.reactivex.Flowable
import java.util.*

data class ProductEntity(private val product: Product,
                         val name: String = product.product,
                         val description: String = product.description) {

    val provider: String by lazy {
        if (TextUtils.isEmpty(product.people)) product.company.name else if (TextUtils.isEmpty(product.people)) product.source else product.people
    }

    val thumbnail: Uri by lazy {
        if (product.productImageList.isEmpty()) Uri.EMPTY
        else Uri.parse(product.productImageList[0].thumbnail)
    }

    val largeImages: List<Uri> by lazy {
        if (product.productImageList.isEmpty()) Collections.emptyList()
        else {
            Flowable.just(product).flatMapIterable { product.productImageList }.flatMapIterable { it.large }.map { Uri.parse(it) }.toList().blockingGet()
        }
    }

    val mediumImages: List<Uri> by lazy {
        if (product.productImageList.isEmpty()) Collections.emptyList()
        else {
            Flowable.just(product).flatMapIterable { product.productImageList }.flatMapIterable { it.medium }.map { Uri.parse(it) }.toList().blockingGet()
        }
    }

    val smallImages: List<Uri> by lazy {
        if (product.productImageList.isEmpty()) Collections.emptyList()
        else {
            Flowable.just(product).flatMapIterable { product.productImageList }.flatMapIterable { it.small }.map { Uri.parse(it) }.toList().blockingGet()
        }
    }
}