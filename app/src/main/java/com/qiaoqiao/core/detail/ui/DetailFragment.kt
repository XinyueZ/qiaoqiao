package com.qiaoqiao.core.detail.ui

import android.content.Context
import android.databinding.DataBindingUtil
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v4.view.ViewCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.graphics.Palette
import android.text.TextUtils
import android.view.*
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ImageView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.qiaoqiao.R
import com.qiaoqiao.app.GlideApp
import com.qiaoqiao.core.detail.DetailContract
import com.qiaoqiao.core.detail.DetailPresenter
import com.qiaoqiao.databinding.FragmentDetailBinding
import com.qiaoqiao.repository.backend.model.wikipedia.Image
import com.qiaoqiao.repository.backend.model.wikipedia.LangLink
import com.qiaoqiao.utils.DeviceUtils
import java.lang.ref.WeakReference

class DetailFragment : Fragment(), DetailContract.View,
        AppBarLayout.OnOffsetChangedListener,
        Palette.PaletteAsyncListener {

    private val LAYOUT = R.layout.fragment_detail
    private var presenter: DetailContract.Presenter? = null
    private var binding: FragmentDetailBinding? = null
    private var contextWeakReference: WeakReference<Context>? = null
    private var multiLanguageMenuItem: MenuItem? = null
    private var photo: Image? = null
    private var previewImage: Image? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater!!, LAYOUT, container, false)
        binding?.let {
            it.fragment = this
        }
        setHasOptionsMenu(true)
        return binding?.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_detail, menu)
        multiLanguageMenuItem = menu.findItem(R.id.action_multi_language_spinner)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        toggleContentLoading()
        setRefreshing(false)
        with(context) {
            contextWeakReference = WeakReference(this)
            val actionBarHeight = calcActionBarHeight(this)

            binding?.let {
                with(it.loadingPb) {
                    setColorSchemeResources(R.color.colorPrimary, R.color.colorPrimaryDark, R.color.colorAccent)
                    setProgressViewEndTarget(true, actionBarHeight * 2)
                    setProgressViewOffset(false, 0, actionBarHeight * 2)
                }


                with(it.appbar) {
                    layoutParams.height = Math.ceil((DeviceUtils.getScreenSize(context).Height * 0.618f).toDouble()).toInt()
                    addOnOffsetChangedListener(this@DetailFragment)
                    val transName = activity.intent.getStringExtra(getString(R.string.transition_share_item_name))
                    if (!TextUtils.isEmpty(transName)) {
                        ViewCompat.setTransitionName(this, transName)
                    }
                }

                with(it.content) {
                    settings.defaultTextEncodingName = "utf-8"
                    webViewClient = object : WebViewClient() {
                        override fun shouldOverrideUrlLoading(v: WebView, request: WebResourceRequest): Boolean {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                v.loadUrl(request.url
                                        .toString())
                            }
                            return super.shouldOverrideUrlLoading(v, request)
                        }

                        @Suppress("OverridingDeprecatedMember")
                        override fun shouldOverrideUrlLoading(v: WebView, url: String): Boolean {
                            v.loadUrl(url)
                            return super.shouldOverrideUrlLoading(v, url)
                        }

                        override fun onPageFinished(v: WebView, url: String) {
                            toggleLoaded()
                            loadDetailImage()
                            super.onPageFinished(v, url)
                        }
                    }
                }
                //Detail has been defined in DetailActivity in xml directly,
                //onViewCreated called earlier than injection.
            }
        }
    }

    override fun loadDetail() {
        presenter?.let {
            val keyword = activity.intent.getStringExtra(EXTRAS_KEYWORD)
            when (!TextUtils.isEmpty(keyword)) {
                true -> it.loadDetail(keyword)
                else -> {
                    val pageId = activity.intent.getIntExtra(EXTRAS_PAGE_ID, -1)
                    it.loadDetail(pageId)
                }
            }
        }
    }

    override fun onDestroyView() {
        binding?.let {
            it.appbar?.removeOnOffsetChangedListener(this)
        }
        super.onDestroyView()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        when (activity is AppCompatActivity) {
            true -> {
                val activity = activity as AppCompatActivity
                activity.setSupportActionBar(binding?.toolbar)
                activity.supportActionBar?.setDisplayHomeAsUpEnabled(true)
            }
        }
    }

    override fun setPresenter(presenter: DetailPresenter) {
        this.presenter = presenter
    }

    override fun setMultiLanguage(langLinks: Array<LangLink>?) {
        if (langLinks == null || langLinks.isEmpty()) {
            return
        }
        for (langLink in langLinks) {
            multiLanguageMenuItem?.let {
                it.subMenu
                        .add(langLink.toString())
                        .setOnMenuItemClickListener({ _ ->
                            if (presenter != null) {
                                presenter!!.loadDetail(langLink)
                            }
                            true
                        })
            }
        }
    }

    override fun getBinding(): FragmentDetailBinding {
        return binding!!
    }

    private fun toggleContentLoading() {
        binding?.let {
            it.layoutLoading?.let {
                it.loadingPb.visibility = View.VISIBLE
            }
        }
    }

    private fun toggleLoaded() {
        binding?.let {
            it.layoutLoading?.let {
                it.loadingPb.visibility = View.GONE
            }
        }
    }

    override fun setDetailImages(preview: Image?, photo: Image?) {
        this.photo = photo
        previewImage = preview
    }

    private fun detailImageLoaded(resource: Bitmap) {
        setRefreshing(false)
        createPalette(resource)
    }

    private fun createPalette(resource: Bitmap) {
        with(Palette.Builder(resource)) {
            maximumColorCount(1)
            generate(this@DetailFragment)
        }
    }

    override fun setText(title: String, content: String) {
        binding?.let {
            it.collapsingToolbar.title = title
            it.content.loadData(content, "text/html; charset=utf-8", "utf-8")
        }
    }

    override fun onOffsetChanged(appBarLayout: AppBarLayout, verticalOffset: Int) {
        if (Math.abs(verticalOffset) == appBarLayout.totalScrollRange) {
            loadDetailPreview(binding!!.previewIv)
        } else {
            binding!!.previewIv.visibility = View.GONE
        }
    }

    private fun loadDetailPreview(imageView: ImageView) {
        if (contextWeakReference!!.get() != null && previewImage != null && previewImage!!.source != null && !activity.isFinishing) {
            GlideApp.with(contextWeakReference!!.get())
                    .asBitmap()
                    .load(previewImage!!.source)
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .skipMemoryCache(false)
                    .placeholder(R.drawable.ic_default_image)
                    .error(R.drawable.ic_default_image)
                    .listener(object : RequestListener<Bitmap> {
                        override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Bitmap>?, isFirstResource: Boolean): Boolean {
                            previewLoadedFail()
                            return true
                        }

                        override fun onResourceReady(resource: Bitmap, model: Any, target: com.bumptech.glide.request.target.Target<Bitmap>?, dataSource: DataSource, isFirstResource: Boolean): Boolean {
                            previewLoaded(imageView)
                            return false
                        }
                    })
                    .into(imageView)
        }
    }

    private fun loadDetailImage() {
        if (contextWeakReference!!.get() == null || photo == null || photo!!.source == null || activity.isFinishing) {
            return
        }
        setRefreshing(true)
        GlideApp.with(contextWeakReference!!.get())
                .asBitmap()
                .load(photo!!.source)
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .error(R.drawable.ic_default_image)
                .placeholder(R.drawable.ic_default_image)
                .skipMemoryCache(false)
                .listener(object : RequestListener<Bitmap> {
                    override fun onLoadFailed(e: GlideException?, model: Any?, target: com.bumptech.glide.request.target.Target<Bitmap>?, isFirstResource: Boolean): Boolean {
                        loadDetailPreview(binding!!.detailIv)
                        return true
                    }

                    override
                    fun onResourceReady(resource: Bitmap, model: Any, target: com.bumptech.glide.request.target.Target<Bitmap>?, dataSource: DataSource, isFirstResource: Boolean): Boolean {
                        detailImageLoaded(resource)
                        return false
                    }
                })
                .into(binding!!.detailIv)
    }

    private fun previewLoadedFail() {
        Snackbar.make(binding!!.root, R.string.no_image, Toast.LENGTH_SHORT).show()
        setRefreshing(false)
    }

    private fun previewLoaded(imageView: ImageView) {
        imageView.visibility = View.VISIBLE
        setRefreshing(false)
    }

    override fun onGenerated(palette: Palette) {
        binding?.let {
            when (palette.swatches.isEmpty()) {
                true -> return
                else -> {
                    with(palette.swatches[0]) {
                        val textColor = bodyTextColor
                        val barColor = rgb
                        with(it.collapsingToolbar) {
                            setExpandedTitleColor(textColor)
                            setCollapsedTitleTextColor(textColor)
                            setContentScrimColor(barColor)
                            setStatusBarScrimColor(barColor)
                        }
                    }
                }
            }
        }
    }

    private fun calcActionBarHeight(cxt: Context): Int {
        val abSzAttr = intArrayOf(android.R.attr.actionBarSize)
        val a = cxt.obtainStyledAttributes(abSzAttr)
        val ret = a.getDimensionPixelSize(0, -1)
        a.recycle()
        return ret
    }

    private fun setRefreshing(refresh: Boolean) {
        binding?.let {
            with(it.loadingPb) {
                isEnabled = refresh
                isRefreshing = refresh
            }
        }
    }

    override fun onError() {
        Snackbar.make(binding!!.root, R.string.loading_detail_fail, Snackbar.LENGTH_INDEFINITE)
                .setAction(android.R.string.ok, { _ -> activity.supportFinishAfterTransition() })
                .show()
        setRefreshing(false)
    }

    override fun onStop() {
        super.onStop()
        binding?.let {
            Glide.with(it.detailIv).clear(it.detailIv)
            Glide.with(it.previewIv).clear(it.previewIv)
        }
    }
}