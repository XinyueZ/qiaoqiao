package com.qiaoqiao.core.product.ui

import android.text.TextUtils
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.qiaoqiao.repository.backend.model.product.Product

@android.databinding.BindingAdapter("bind:product")
fun setProductOnRecyclerView(imageView: ImageView, product: Product) {
}

@android.databinding.BindingAdapter("bind:autoText")
fun setAutoText(textView: TextView, someText: String) {
    when {
        TextUtils.isEmpty(someText) || TextUtils.isEmpty(someText.trim()) -> textView.visibility = View.GONE
        else -> {
            textView.visibility = View.VISIBLE
            textView.text = someText
        }
    }
}


