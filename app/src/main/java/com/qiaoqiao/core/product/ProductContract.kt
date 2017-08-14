package com.qiaoqiao.core.product

import com.google.android.gms.vision.barcode.Barcode
import com.qiaoqiao.databinding.FragmentProductListBinding
import com.qiaoqiao.mvp.BasePresenter
import com.qiaoqiao.mvp.BaseView
import com.qiaoqiao.repository.backend.model.product.Products

interface ProductContract {

    interface ListView : BaseView<ListPresenter, FragmentProductListBinding> {
        override fun getBinding(): FragmentProductListBinding
        fun showProductList(products: Products)
    }

    interface ListPresenter : BasePresenter {
        fun showProductList(barcode: Barcode)
    }
}