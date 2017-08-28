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
import com.qiaoqiao.R
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
        val searchedCode: Barcode = arguments.getParcelable(EXTRAS_BARCODE)
        presenter?.showProductList(searchedCode)
        binding?.let {
            it.productListRv.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
            it.productListRv.setHasFixedSize(true)
            if (activity is AppCompatActivity) {
                val activity = activity as AppCompatActivity
                activity.setSupportActionBar(it.toolbar)
                activity.supportActionBar?.setDisplayHomeAsUpEnabled(true)
                activity.supportActionBar?.setDisplayShowHomeEnabled(true)

            }
            it.loadingPbTv.text = String.format(getString(R.string.loading_product_by_upc), searchedCode.rawValue)
        }
    }

    override fun getBinding() = binding!!

    override fun setPresenter(presenter: ProductContract.ListPresenter) {
        this.presenter = presenter
    }

    override fun showProductList(products: List<ProductEntity>) {
        binding?.let {
            it.productListRv.adapter = ProductListAdapter(products)
            it.showProduct = true
        }
    }
}