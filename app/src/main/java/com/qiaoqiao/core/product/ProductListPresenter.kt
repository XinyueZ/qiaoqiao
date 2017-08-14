package com.qiaoqiao.core.product

import android.support.v4.app.FragmentActivity
import com.google.android.gms.vision.barcode.Barcode
import com.qiaoqiao.R
import com.qiaoqiao.core.product.ui.ProductListFragment
import com.qiaoqiao.repository.DsLoadedCallback
import com.qiaoqiao.repository.DsRepository
import com.qiaoqiao.repository.backend.model.product.Products
import javax.inject.Inject

class ProductListPresenter @Inject constructor(private var view: ProductContract.ListView,
                                               private var dsRepository: DsRepository) : ProductContract.ListPresenter {

    @Inject
    fun onInjected() {
        view.setPresenter(this)
    }

    override fun begin(hostActivity: FragmentActivity) {
        val keyword = hostActivity.intent.getStringExtra(com.qiaoqiao.core.product.ui.ProductListActivity.Companion.EXTRAS_KEYWORD)
        val barcode = hostActivity.intent.getParcelableExtra<Barcode>(com.qiaoqiao.core.product.ui.ProductListActivity.Companion.EXTRAS_BARCODE)
        val fragment = view as ProductListFragment
        fragment.arguments.putParcelable(com.qiaoqiao.core.product.ui.ProductListFragment.Companion.EXTRAS_BARCODE, barcode)
        hostActivity.supportFragmentManager.beginTransaction()
                .add(R.id.container,
                        fragment,
                        fragment.javaClass.name).commit()
    }

    override fun end(hostActivity: FragmentActivity) {
    }

    override fun showProductList(barcode: Barcode) {
        dsRepository.onKnowledgeQuery(barcode, object : DsLoadedCallback() {
            override fun onKnowledgeResponse(products: Products) {
                view.showProductList(products)
            }
        })
    }

}