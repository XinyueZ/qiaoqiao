package com.qiaoqiao.core.product.ui

import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.qiaoqiao.R
import com.qiaoqiao.app.App
import com.qiaoqiao.core.product.ProductListPresenter
import javax.inject.Inject

private const val LAYOUT = R.layout.activity_product_list

class ProductListActivity : AppCompatActivity() {
    @Inject lateinit var presenter: ProductListPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DataBindingUtil.setContentView<ViewDataBinding>(this, LAYOUT)
        App.inject(this)
    }

    @Inject
    fun injected() {
        presenter.begin(this)
    }

    override fun onDestroy() {
        presenter.end(this)
        super.onDestroy()
    }
}