package com.qiaoqiao.core.product

import com.qiaoqiao.databinding.FragmentProductListBinding
import com.qiaoqiao.mvp.BasePresenter
import com.qiaoqiao.mvp.BaseView

interface ProductContract {

    interface ListView : BaseView<ListPresenter, FragmentProductListBinding> {
        override fun getBinding(): FragmentProductListBinding
        fun showProductList(keyword: String)
    }

    interface ListPresenter : BasePresenter {
        fun showProductList(keyword: String)
    }
}