package com.qiaoqiao.core.product

import android.content.Context
import com.qiaoqiao.core.product.annotation.ProductScoped
import com.qiaoqiao.core.product.ui.ProductListFragment
import dagger.Module
import dagger.Provides

@Module
class ProductModule {
    @ProductScoped
    @Provides
    fun provideProductContractListView(cxt: Context): ProductContract.ListView = ProductListFragment.newInstance(cxt)

}