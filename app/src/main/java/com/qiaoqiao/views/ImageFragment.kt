package com.qiaoqiao.views

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.qiaoqiao.core.product.ui.ProductListFragment
import com.qiaoqiao.databinding.FragmentImageBinding

class ImageFragment : Fragment() {
    private var binding: FragmentImageBinding? = null

    companion object {
        val EXTRAS_IMAGE_URI = ProductListFragment::class.java.name + ".EXTRAS.uri"
        fun newInstance(cxt: Context, imageUri: Uri): ImageFragment {
            val args = Bundle(1)
            args.putParcelable(EXTRAS_IMAGE_URI, imageUri)
            return Fragment.instantiate(cxt, ImageFragment::class.java.name, args) as ImageFragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentImageBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.let {
            it.imageUri = arguments.getParcelable(EXTRAS_IMAGE_URI)
            it.executePendingBindings()
        }
    }
}