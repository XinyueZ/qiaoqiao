package com.qiaoqiao.core.product.ui

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.vision.barcode.Barcode
import com.qiaoqiao.core.product.ProductContract
import com.qiaoqiao.core.product.model.ProductEntity
import com.qiaoqiao.databinding.FragmentProductListBinding

class ProductListFragment : Fragment(), ProductContract.ListView {
    private var presenter: ProductContract.ListPresenter? = null
    private var binding: FragmentProductListBinding? = null

    companion object {
        val EXTRAS_KEYWORD = ProductListFragment::class.java.name + ".EXTRAS.keyword"
        val EXTRAS_BARCODE = ProductListFragment::class.java.name + ".EXTRAS.barcode"
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
        presenter?.showProductList(arguments.getParcelable<Barcode>(EXTRAS_BARCODE))
        binding?.let {
            it.productListRv.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
            it.productListRv.setHasFixedSize(true)
            it.shimmerFl.startShimmerAnimation()
            if (activity is AppCompatActivity) {
                (activity as AppCompatActivity).setSupportActionBar(it.toolbar)
                (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
                (activity as AppCompatActivity).supportActionBar?.setDisplayShowHomeEnabled(true)

            }
        }
    }

    override fun getBinding() = binding!!

    override fun setPresenter(presenter: ProductContract.ListPresenter) {
        this.presenter = presenter
    }

    override fun showProductList(products: List<ProductEntity>) {
        binding?.let {
            it.productListRv.adapter = ProductListAdapter(products)
            it.shimmerFl.stopShimmerAnimation()
            it.showProduct = true
        }
    }
}