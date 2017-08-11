package com.qiaoqiao.core.product.ui

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.qiaoqiao.databinding.ItemProductListBinding
import com.qiaoqiao.repository.backend.model.product.Products

internal class ProductListAdpater(private val products: Products) : RecyclerView.Adapter<ProductListAdapterViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int) = ProductListAdapterViewHolder(ItemProductListBinding.inflate(LayoutInflater.from(parent?.context)))
    override fun getItemCount() = products.result.size
    override fun onBindViewHolder(holder: ProductListAdapterViewHolder?, position: Int) {
        holder?.let {
            it.binding.product = products.result[position]
            it.binding.executePendingBindings()
        }
    }
}

internal class ProductListAdapterViewHolder(internal val binding: ItemProductListBinding) : RecyclerView.ViewHolder(binding.root)