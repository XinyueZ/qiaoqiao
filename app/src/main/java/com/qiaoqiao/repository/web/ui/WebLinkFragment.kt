package com.qiaoqiao.repository.web.ui

import android.app.Activity
import android.content.Intent
import android.databinding.DataBindingUtil
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.qiaoqiao.R
import com.qiaoqiao.databinding.FragmentWebLinkBinding

class WebLinkFragment : Fragment(), View.OnClickListener {
    private val LAYOUT = R.layout.fragment_web_link
    private var binding: FragmentWebLinkBinding? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, LAYOUT, container, false)
        binding?.uriTv?.setText(R.string.demo_web)
        binding?.takeUriBtn?.setOnClickListener(this)
        return binding?.root
    }

    override fun onClick(v: View) {
        val data = Intent()
        data.data = Uri.parse(binding?.uriTv?.text.toString())
        with(activity) {
            setResult(Activity.RESULT_OK, data)
            supportFinishAfterTransition()
        }
    }
}