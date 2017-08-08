package com.qiaoqiao.core.product

import android.support.v4.app.FragmentActivity
import com.qiaoqiao.repository.DsRepository
import javax.inject.Inject

class ProductListPresenter @Inject constructor(private var view: ProductContract.ListView,
                                               private var dsRepository: DsRepository) : ProductContract.ListPresenter {

    @Inject
    fun onInjected() {
        view.setPresenter(this)
    }

    override fun begin(hostActivity: FragmentActivity) {
    }

    override fun end(hostActivity: FragmentActivity) {
    }
}