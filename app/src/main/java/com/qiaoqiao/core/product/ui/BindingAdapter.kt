package com.qiaoqiao.core.product.ui

import android.net.Uri
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.text.Html
import android.text.TextUtils
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.qiaoqiao.R
import com.qiaoqiao.app.GlideApp
import com.qiaoqiao.core.product.model.ProductEntity
import com.qiaoqiao.views.ImageFragment
import com.qiaoqiao.views.ViewPagerAdapter
import io.reactivex.Flowable

@android.databinding.BindingAdapter("bind:product")
fun setProductImageList(vp: ViewPager, product: ProductEntity?) {
    if (product == null) return
    Flowable.just(product).flatMapIterable { product.largeImages }.map { ImageFragment.newInstance(vp.context, it) }.toList().subscribe( { fragmentList, _ ->
        vp.adapter = ViewPagerAdapter((vp.context as AppCompatActivity).supportFragmentManager, fragmentList)
    })
}

@android.databinding.BindingAdapter("bind:imageUri")
fun setImageViewUrl(imageView: ImageView, imageUri: Uri?) {
    if (imageUri == null) return
    GlideApp.with(imageView.context)
            .load(imageUri)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .skipMemoryCache(false)
            .placeholder(R.drawable.ic_default_image)
            .error(R.drawable.ic_default_image)
            .into(imageView)
}

@android.databinding.BindingAdapter("bind:autoText")
fun setAutoText(textView: TextView, someText: String?) {
    when {
        TextUtils.isEmpty(someText) || TextUtils.isEmpty(someText?.trim()) -> textView.visibility = View.GONE
        else -> {
            textView.visibility = View.VISIBLE
            textView.text = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                Html.fromHtml(someText, Html.FROM_HTML_MODE_LEGACY)
            } else {
                Html.fromHtml(someText)
            }
        }
    }
}


