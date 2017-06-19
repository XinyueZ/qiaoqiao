package com.qiaoqiao.core.camera.history.ui

import android.content.res.Configuration
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.qiaoqiao.R
import com.qiaoqiao.core.camera.history.HistoryContract
import com.qiaoqiao.core.camera.history.HistoryPresenter2
import com.qiaoqiao.databinding.FragmentStackviewHistoryBinding
import com.qiaoqiao.repository.database.HistoryItem

private const val LAYOUT: Int = R.layout.fragment_stackview_history

class HistoryStackViewFragment : Fragment(), HistoryContract.View2 {
    private var binding: FragmentStackviewHistoryBinding? = null
    private var presenter: HistoryContract.Presenter2? = null
    private var adapter: HistoryStackViewAdapter? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate<FragmentStackviewHistoryBinding>(inflater, LAYOUT, container, false)
        return binding?.root
    }

    override fun setPresenter(presenter: HistoryPresenter2) {
        this.presenter = presenter
    }

    override fun getBinding() = binding

    override fun showList(results: MutableList<HistoryItem>) {
        adapter = HistoryStackViewAdapter(results, activity.layoutInflater)
        binding?.historyStv?.adapter = adapter
        binding?.historyStv?.setSelection(results.size - 1)
    }

    override fun updateList(historyItemList: MutableList<HistoryItem>) {
        adapter?.notifyDataSetChanged()
        binding?.historyStv?.visibility = if (!historyItemList.isEmpty()) {
            View.VISIBLE
        } else
            View.GONE

        if (historyItemList.isEmpty()) return
        binding?.historyStv?.setSelection(historyItemList.size - 1)
    }

    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)
        adapter?.notifyDataSetChanged()
    }
}