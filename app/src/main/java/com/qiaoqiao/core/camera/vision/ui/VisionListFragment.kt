package com.qiaoqiao.core.camera.vision.ui

import android.content.Context
import android.content.res.Configuration
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.content.res.AppCompatResources
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.GridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.qiaoqiao.R
import com.qiaoqiao.app.Key
import com.qiaoqiao.core.camera.vision.VisionContract
import com.qiaoqiao.core.camera.vision.model.VisionEntity
import com.qiaoqiao.databinding.FragmentListVisionBinding

private const val LAYOUT = R.layout.fragment_list_vision
private val EXTRAS_KEY = VisionListFragment::class.java.name + ".EXTRAS.key"

class VisionListFragment : AbstractVisionFragment(), VisionContract.View {
    private lateinit var binding: FragmentListVisionBinding
    private lateinit var visionListAdapter: VisionListAdapter
    private var presenter: VisionContract.Presenter? = null

    companion object {
        fun newInstance(cxt: Context, key: Key): VisionListFragment {
            val args = Bundle(1)
            args.putSerializable(EXTRAS_KEY, key)
            return Fragment.instantiate(cxt, VisionListFragment::class.java.name, args) as VisionListFragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, LAYOUT, container, false)
        binding.fragment = this
        return binding.root
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setRefreshing(false)
        val key = arguments.getSerializable(EXTRAS_KEY) as Key
        binding.loadingPb.setColorSchemeResources(R.color.colorPrimary, R.color.colorPrimaryDark, R.color.colorAccent)
        val columns = resources.getInteger(R.integer.num_columns)
        binding.visionRv.layoutManager = GridLayoutManager(activity, columns)
        visionListAdapter = VisionListAdapter(key)
        binding.visionRv.adapter = visionListAdapter
        val dividerItemDecoration = DividerItemDecoration(activity, DividerItemDecoration.VERTICAL)
        val divideDrawable = AppCompatResources.getDrawable(activity, R.drawable.divider_drawable)
        if (divideDrawable != null) {
            dividerItemDecoration.setDrawable(divideDrawable)
        }
        binding.visionRv.addItemDecoration(dividerItemDecoration)
        presenter?.loadRecent()
    }

    override fun getBinding() = this.binding

    override fun setPresenter(presenter: VisionContract.Presenter) {
        this.presenter = presenter
    }

    override fun clear() {
        visionListAdapter.clear()
    }

    override fun addEntities(visionEntityList: MutableList<VisionEntity>) {
        visionListAdapter.addVisionEntityList(visionEntityList)
        setRefreshing(false)
    }

    override fun showDetail(entity: VisionEntity, transitionView: View) {
        openDetail(entity, transitionView)
    }

    override fun setRefreshing(refresh: Boolean) {
        binding.loadingPb.isEnabled = refresh
        binding.loadingPb.isRefreshing = refresh
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        val columns = resources.getInteger(R.integer.num_columns)
        binding.visionRv.layoutManager = GridLayoutManager(activity, columns)
        binding.visionRv.adapter.notifyDataSetChanged()
    }
}