package com.qiaoqiao.core.product

import com.qiaoqiao.app.AppComponent
import com.qiaoqiao.core.product.annotation.ProductScoped
import com.qiaoqiao.core.product.ui.ProductListActivity
import com.qiaoqiao.repository.DsRepositoryComponent
import dagger.Component

@ProductScoped
@Component(dependencies = arrayOf(DsRepositoryComponent::class, AppComponent::class),
        modules = arrayOf(ProductModule::class))
interface ProductComponent {
    fun injectProductList(activity: ProductListActivity)
}