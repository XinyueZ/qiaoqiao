package com.qiaoqiao.core.product.ui

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.qiaoqiao.core.product.ProductContract
import com.qiaoqiao.databinding.FragmentProductListBinding

class ProductListFragment : Fragment(), ProductContract.ListView {
    private var presenter: ProductContract.ListPresenter? = null
    private var binding: FragmentProductListBinding? = null

    companion object {
        val EXTRAS_KEYWORD = ProductListFragment::class.java.name + ".EXTRAS.keyword"
        fun newInstance(cxt: Context): ProductListFragment = instantiate(cxt, ProductListFragment::class.java.name, Bundle()) as ProductListFragment
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentProductListBinding.inflate(inflater, container, false)
        binding?.fragment = this
        setHasOptionsMenu(true)
        return binding?.root
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter?.showProductList(arguments.getString(EXTRAS_KEYWORD))
    }

    override fun showProductList(keyword: String) {
        getBinding().testTv.text = keyword
    }

    override fun getBinding() = binding!!

    override fun setPresenter(presenter: ProductContract.ListPresenter) {
        this.presenter = presenter
    }
}