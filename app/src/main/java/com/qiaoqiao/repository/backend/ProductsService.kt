package com.qiaoqiao.repository.backend

import com.qiaoqiao.repository.backend.model.product.Product
import com.qiaoqiao.repository.backend.model.product.Products
import io.reactivex.Flowable
import retrofit2.http.Body
import retrofit2.http.POST

interface ProductsService {
    @POST("product/upc")
    fun getProducts(@Body body: KnowledgeRequest): Flowable<Products>

    @POST("product/upc/detail")
    fun getProduct(@Body body: KnowledgeRequest): Flowable<Product>
}