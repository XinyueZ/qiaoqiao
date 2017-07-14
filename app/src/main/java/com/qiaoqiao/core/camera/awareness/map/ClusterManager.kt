package com.qiaoqiao.core.camera.awareness.map

import android.support.v4.app.FragmentActivity
import com.google.android.gms.maps.GoogleMap
import com.google.maps.android.MarkerManager
import com.google.maps.android.clustering.ClusterItem
import com.qiaoqiao.core.camera.awareness.bus.OpenClusterItemEvent
import de.greenrobot.event.EventBus

class ClusterManager private constructor(activity: FragmentActivity, map: GoogleMap)
    : com.google.maps.android.clustering.ClusterManager<ClusterItem>(activity.applicationContext, map, MarkerManager(map)), com.google.maps.android.clustering.ClusterManager.OnClusterItemClickListener<ClusterItem> {
    init {
        map.setOnMarkerClickListener(this)
        renderer = ClusterRenderer(activity.applicationContext, map, this)
        renderer.setAnimation(true)
        setOnClusterItemClickListener(this)
    }

    companion object {
        fun showGeosearch(activity: FragmentActivity?, googleMap: GoogleMap, clusterItems: List<ClusterItem>) {
            when (activity) {
                null -> return
                else -> {
                    val ret = ClusterManager(activity, googleMap)
                    ret.addItems(clusterItems)
                }
            }
        }
    }

    override fun onClusterItemClick(clusterItem: ClusterItem): Boolean {
        EventBus.getDefault().post(OpenClusterItemEvent(clusterItem))
        return true
    }
}