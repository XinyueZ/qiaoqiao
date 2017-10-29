package com.qiaoqiao.core.product

import android.support.v4.app.FragmentActivity
import com.google.android.gms.vision.barcode.Barcode
import com.qiaoqiao.R
import com.qiaoqiao.core.product.model.ProductEntity
import com.qiaoqiao.core.product.ui.ProductListFragment
import com.qiaoqiao.repository.DsLoadedCallback
import com.qiaoqiao.repository.DsRepository
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import javax.inject.Inject

class ProductListPresenter @Inject constructor(private var view: ProductContract.ListView,
                                               private var dsRepository: DsRepository) : ProductContract.ListPresenter {

    private val compositeDisposable = CompositeDisposable()
    @Inject
    fun onInjected() {
        view.setPresenter(this)
    }

    private fun addToAutoDispose(vararg disposables: Disposable) {
        compositeDisposable.addAll(*disposables)
    }

    private fun autoDispose() {
        compositeDisposable.clear()
    }

    override fun begin(hostActivity: FragmentActivity) {
        val keyword = hostActivity.intent.getStringExtra(com.qiaoqiao.core.product.ui.ProductListActivity.Companion.EXTRAS_KEYWORD)
        val barcode = hostActivity.intent.getParcelableExtra<Barcode>(com.qiaoqiao.core.product.ui.ProductListActivity.Companion.EXTRAS_BARCODE)
        val fragment = view as ProductListFragment
        fragment.arguments?.let {
            it.putParcelable(com.qiaoqiao.core.product.ui.ProductListFragment.Companion.EXTRAS_BARCODE, barcode)
            hostActivity.supportFragmentManager.beginTransaction()
                    .add(R.id.container,
                            fragment,
                            fragment.javaClass.name).commit()
        }
    }

    override fun end(hostActivity: FragmentActivity) {
        autoDispose()
    }

    override fun showProductList(barcode: Barcode) {
        addToAutoDispose(dsRepository.onKnowledgeQuery(barcode, object : DsLoadedCallback() {
            override fun onKnowledgeResponse(product: ProductEntity) {
                view.showProductList(product)
            }
        }))
    }

}