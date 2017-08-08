package com.qiaoqiao.core.product.ui

import android.app.Activity
import android.content.Intent
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.os.Bundle
import android.support.v4.app.ActivityCompat.startActivity
import android.support.v7.app.AppCompatActivity
import com.qiaoqiao.R
import com.qiaoqiao.app.App
import com.qiaoqiao.core.product.ProductContract
import com.qiaoqiao.core.product.ProductListPresenter
import javax.inject.Inject

private const val LAYOUT = R.layout.activity_product_list

class ProductListActivity : AppCompatActivity() {
    @Inject lateinit var presenter: ProductListPresenter
    @Inject lateinit var productListFragment: ProductContract.ListView

    companion object {
        val EXTRAS_KEYWORD = ProductListActivity::class.java.name + ".EXTRAS.keyword"
        fun showInstance(cxt: Activity, keyword: String) {
            val intent = Intent(cxt, ProductListActivity::class.java)
            intent.putExtra(ProductListActivity.EXTRAS_KEYWORD, keyword)
            intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(cxt, intent, Bundle.EMPTY)
        }
    }

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