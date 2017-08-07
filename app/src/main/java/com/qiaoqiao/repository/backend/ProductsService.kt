package com.qiaoqiao.repository.backend

import com.qiaoqiao.repository.backend.model.product.Products
import io.reactivex.Observable
import retrofit2.http.GET

interface ProductsService {
    @GET("product/upc")
    fun getProducts(): Observable<Products>
}