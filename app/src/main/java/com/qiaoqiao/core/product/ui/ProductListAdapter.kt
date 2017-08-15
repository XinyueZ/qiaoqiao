package com.qiaoqiao.core.product.ui

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.qiaoqiao.core.product.model.ProductEntity
import com.qiaoqiao.databinding.ItemProductListBinding

internal class ProductListAdapter(private val products: List<ProductEntity>) : RecyclerView.Adapter<ProductListAdapterViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int) = ProductListAdapterViewHolder(ItemProductListBinding.inflate(LayoutInflater.from(parent?.context)))
    override fun getItemCount() = products.size
    override fun onBindViewHolder(holder: ProductListAdapterViewHolder?, position: Int) {
        holder?.let {
            it.binding.product = products[position]
            it.binding.executePendingBindings()
        }
    }
}

internal class ProductListAdapterViewHolder(internal val binding: ItemProductListBinding) : RecyclerView.ViewHolder(binding.root)