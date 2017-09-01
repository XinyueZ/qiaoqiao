package com.qiaoqiao.core.camera.awareness.ui

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem
import com.qiaoqiao.R
import com.qiaoqiao.core.camera.awareness.AwarenessContract
import com.qiaoqiao.core.camera.awareness.map.ClusterManager
import com.qiaoqiao.databinding.PlacesBinding

private const val LAYOUT = R.layout.fragment_snapshot_places

class SnapshotPlacesFragment : Fragment(), AwarenessContract.View,
        OnMapReadyCallback,
        GoogleMap.OnMapClickListener {
    private var binding: PlacesBinding? = null
    private var presenter: AwarenessContract.Presenter? = null
    private var googleMap: GoogleMap? = null

    companion object {
        fun newInstance(cxt: Context): SnapshotPlacesFragment = instantiate(cxt, SnapshotPlacesFragment::class.java.name) as SnapshotPlacesFragment
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, LAYOUT, container, false)
        binding?.locatingControl?.setOnFromLocalClickedListener { locating() }
        binding?.locatingControl?.setOnAdjustClickedListener { handleAdjustUI() }
        return binding?.root
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        retainInstance = true
        val fragmentById = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        with(fragmentById) {
            onCreate(savedInstanceState)
            onStart()
            getMapAsync(this@SnapshotPlacesFragment)
        }
        presenter?.settingLocating(activity)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter?.end(activity)
    }

    override fun setPresenter(presenter: AwarenessContract.Presenter) {
        this.presenter = presenter
    }

    override fun getBinding() = binding

    override fun onLocatingError() {
        Toast.makeText(activity, R.string.locating_fail, Toast.LENGTH_LONG).show()
        binding?.locatingControl?.stopLocalProgressBar()
    }

    override fun onPlayServiceConnectionFailed() {
        //TODO Impl. not be done
    }

    override fun onMapReady(gm: GoogleMap) {
        gm.setOnMapClickListener(this)
        gm.uiSettings.isMapToolbarEnabled = false
        gm.uiSettings.isMyLocationButtonEnabled = false
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return
        }
        gm.isMyLocationEnabled = true
        googleMap = gm
    }

    override fun locating() {
        binding?.locatingControl?.startLocalProgressBar()
        presenter?.locating(context)
    }

    override fun onLocated(latLng: LatLng) {
        googleMap?.let {
            if (isDetached || !isAdded) {
                return
            }
            it.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, resources.getInteger(R.integer.zoom).toFloat()))
        } ?: kotlin.run {
            val fragmentById = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
            fragmentById.getMapAsync {
                googleMap = it
                onLocated(latLng)
            }
        }
        if (context != null) {
            presenter?.searchAndSearch(context, latLng)
        }
    }

    override fun showAllGeoAndPlaces(clusterItemList: List<ClusterItem>) {
        when (googleMap) {
            null -> {
                val fragmentById = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
                fragmentById.getMapAsync {
                    googleMap = it
                    showAllGeoAndPlaces(clusterItemList)
                }
            }
            else -> {
                ClusterManager.Companion.showGeosearch(activity, googleMap, clusterItemList)
                binding?.locatingControl?.stopLocalProgressBar()
                if (isDetached || !isAdded) {
                    return
                }
                googleMap?.animateCamera(CameraUpdateFactory.zoomTo((resources.getInteger(R.integer.zoom) + 3).toFloat()))
            }
        }
    }

    override fun onMapClick(latLng: LatLng) {
        dismissAdjust()
    }

    private fun dismissAdjust() {
        binding?.let {
            if (it.adjustFl.visibility == View.VISIBLE) {
                it.adjustFl.visibility = GONE
                locating()
            }
        }
    }

    private fun handleAdjustUI() {
        binding?.let {
            it.adjustFl.visibility = if (it.adjustFl.visibility != View.VISIBLE) View.VISIBLE else View.GONE
            if (it.adjustFl.visibility == View.VISIBLE) {
                presenter?.loadGeosearchAdjust(context)
            }
        }
    }

    override fun showAdjust(adjust: Adjust) {
        binding?.let {
            it.adjust = adjust
            it.adjustRadiusSb.thumb = adjust.createThumbDrawable()
        }

    }
}