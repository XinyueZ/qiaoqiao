package com.qiaoqiao.core.detail.ui

import android.app.Activity
import android.content.Intent
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.view.ViewCompat
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.view.View

import com.qiaoqiao.R
import com.qiaoqiao.app.App
import com.qiaoqiao.core.detail.DetailPresenter

import javax.inject.Inject

internal val EXTRAS_KEYWORD = DetailActivity::class.java.name + ".EXTRAS.keyword"
internal val EXTRAS_PAGE_ID = DetailActivity::class.java.name + ".EXTRAS.page.id"

class DetailActivity : AppCompatActivity() {
    private val LAYOUT = R.layout.activity_detail

    @Inject lateinit var mPresenter: DetailPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DataBindingUtil.setContentView<ViewDataBinding>(this, LAYOUT)
        App.inject(this)
    }

    @Inject
    fun injected() {
        mPresenter.begin(this)
    }

    override fun onDestroy() {
        mPresenter.end(this)
        super.onDestroy()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> supportFinishAfterTransition()
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        fun showInstance(cxt: Activity, keyword: String, transitionView: View) {
            val transitionSharedItemName = ViewCompat.getTransitionName(transitionView)
            with(Intent(cxt, DetailActivity::class.java)) {
                putExtra(EXTRAS_KEYWORD, keyword)
                flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
                putExtra(cxt.getString(R.string.transition_share_item_name), transitionSharedItemName)
                val options = ActivityOptionsCompat.makeSceneTransitionAnimation(cxt, transitionView, transitionSharedItemName)
                ActivityCompat.startActivity(cxt, this, options.toBundle())
            }
        }

        fun showInstance(cxt: Activity, pageId: Int) {
            with(Intent(cxt, DetailActivity::class.java)) {
                putExtra(EXTRAS_PAGE_ID, pageId)
                flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
                val optionsCompat = ActivityOptionsCompat.makeBasic()
                ActivityCompat.startActivity(cxt, this, optionsCompat.toBundle())
            }
        }
    }
}
