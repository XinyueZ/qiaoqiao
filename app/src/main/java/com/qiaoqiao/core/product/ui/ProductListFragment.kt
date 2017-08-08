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
        fun newInstance(cxt: Context): ProductListFragment = instantiate(cxt, ProductListFragment::class.java.name) as ProductListFragment
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentProductListBinding.inflate(inflater, container, false)
        binding?.fragment = this
        setHasOptionsMenu(true)
        return binding?.root
    }

    override fun getBinding() = binding!!

    override fun setPresenter(presenter: ProductContract.ListPresenter) {
        this.presenter = presenter
    }
}