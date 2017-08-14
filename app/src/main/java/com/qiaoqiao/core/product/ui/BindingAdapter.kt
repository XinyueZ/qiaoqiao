package com.qiaoqiao.core.product.ui

import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.qiaoqiao.R
import com.qiaoqiao.repository.backend.model.product.Product
import com.qiaoqiao.views.ImageFragment
import com.qiaoqiao.views.ViewPagerAdapter
import io.reactivex.Flowable

@android.databinding.BindingAdapter("bind:product")
fun setProductImageList(vp: ViewPager, product: Product) {
    Flowable.just(product).flatMapIterable { product.productImageList }.flatMapIterable { it.url }.map { ImageFragment.newInstance(vp.context, it) }.toList().subscribe {
        fragmentList, _ ->
        vp.adapter = ViewPagerAdapter((vp.context as AppCompatActivity).supportFragmentManager, fragmentList)
    }
}

@android.databinding.BindingAdapter("bind:imageUrl")
fun setImageViewUrl(imageView: ImageView, imageUrl: String) {
    Glide.with(imageView.context)
            .load(imageUrl)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .skipMemoryCache(false)
            .placeholder(R.drawable.ic_default_image)
            .error(R.drawable.ic_default_image)
            .crossFade()
            .into(imageView)
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


