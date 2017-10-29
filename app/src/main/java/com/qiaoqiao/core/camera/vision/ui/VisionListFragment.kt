package com.qiaoqiao.core.camera.vision.ui

import android.content.Context
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
    private var binding: FragmentListVisionBinding? = null
    private var visionListAdapter: VisionListAdapter? = null
    private var presenter: VisionContract.Presenter? = null

    companion object {
        fun newInstance(cxt: Context, key: Key): VisionListFragment {
            val args = Bundle(1)
            args.putSerializable(EXTRAS_KEY, key)
            return Fragment.instantiate(cxt, VisionListFragment::class.java.name, args) as VisionListFragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, LAYOUT, container, false)
        binding?.fragment = this
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        retainInstance = true
        setRefreshing(false)

        arguments?.let {
            it.apply {
                binding?.let {
                    val key = this@apply.getSerializable(EXTRAS_KEY) as Key
                    val columns = resources.getInteger(R.integer.num_columns)
                    it.visionRv.layoutManager = GridLayoutManager(activity, columns)
                    visionListAdapter = VisionListAdapter(key)
                    it.visionRv.adapter = visionListAdapter
                    val dividerItemDecoration = DividerItemDecoration(activity, DividerItemDecoration.VERTICAL)
                    activity?.let {
                        val divideDrawable = AppCompatResources.getDrawable(it, R.drawable.divider_drawable)
                        if (divideDrawable != null) {
                            dividerItemDecoration.setDrawable(divideDrawable)
                        }
                    }
                    it.visionRv.addItemDecoration(dividerItemDecoration)
                    presenter?.loadRecent()

                }
            }
        }
    }

    override fun getBinding() = this.binding

    override fun setPresenter(presenter: VisionContract.Presenter) {
        this.presenter = presenter
    }

    override fun clear() {
        visionListAdapter?.clear()
    }

    override fun addEntities(visionEntityList: MutableList<VisionEntity>) {
        visionListAdapter?.addVisionEntityList(visionEntityList)
        setRefreshing(false)
    }

    override fun showDetail(entity: VisionEntity, transitionView: View) {
        openDetail(entity, transitionView)
    }

    override fun setRefreshing(refresh: Boolean) {
        binding?.let {
            it.loadingPb.visibility = if (refresh) View.VISIBLE else View.GONE
        }
    }
}