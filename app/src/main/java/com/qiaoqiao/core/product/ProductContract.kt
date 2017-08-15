package com.qiaoqiao.core.product

import com.google.android.gms.vision.barcode.Barcode
import com.qiaoqiao.core.product.model.ProductEntity
import com.qiaoqiao.databinding.FragmentProductListBinding
import com.qiaoqiao.mvp.BasePresenter
import com.qiaoqiao.mvp.BaseView

interface ProductContract {

    interface ListView : BaseView<ListPresenter, FragmentProductListBinding> {
        override fun getBinding(): FragmentProductListBinding
        fun showProductList(products: List<ProductEntity>)
    }

    interface ListPresenter : BasePresenter {
        fun showProductList(barcode: Barcode)
    }
}