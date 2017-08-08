package com.qiaoqiao.core.product

import android.support.v4.app.FragmentActivity
import com.qiaoqiao.R
import com.qiaoqiao.core.product.ui.ProductListFragment
import com.qiaoqiao.repository.DsRepository
import javax.inject.Inject

class ProductListPresenter @Inject constructor(private var view: ProductContract.ListView,
                                               private var dsRepository: DsRepository) : ProductContract.ListPresenter {

    @Inject
    fun onInjected() {
        view.setPresenter(this)
    }

    override fun showProductList(keyword: String) {
        view.showProductList(keyword)
    }

    override fun begin(hostActivity: FragmentActivity) {
        val keyword = hostActivity.intent.getStringExtra(com.qiaoqiao.core.product.ui.ProductListActivity.Companion.EXTRAS_KEYWORD)
        val fragment = view as ProductListFragment
        fragment.arguments.putString(com.qiaoqiao.core.product.ui.ProductListFragment.Companion.EXTRAS_KEYWORD, keyword)
        hostActivity.supportFragmentManager.beginTransaction()
                .add(R.id.container,
                        fragment,
                        fragment.javaClass.name).commit()
    }

    override fun end(hostActivity: FragmentActivity) {
    }
}