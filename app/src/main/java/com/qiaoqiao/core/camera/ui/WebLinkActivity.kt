package com.qiaoqiao.core.camera.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.app.ActivityOptionsCompat
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.qiaoqiao.R

const val REQ_WEB_LINK = 0x80

class WebLinkActivity : AppCompatActivity() {
    private val LAYOUT = R.layout.activity_web_link

    companion object {

        fun showInstance(cxt: Activity, transitionView: View?) {
            val intent = Intent(cxt, WebLinkActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
            ActivityCompat.startActivityForResult(cxt, intent, REQ_WEB_LINK, if (transitionView != null) {
                ActivityOptionsCompat.makeSceneTransitionAnimation(cxt, transitionView, cxt.getString(R.string.transition_share_item_name))
            } else {
                ActivityOptionsCompat.makeBasic()
            }.toBundle())
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(LAYOUT)
    }
}