package com.qiaoqiao.core.camera.awareness.map

import android.graphics.Bitmap
import com.google.android.gms.location.places.Place
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem
import com.qiaoqiao.customtabs.CustomTabUtils
import java.io.Serializable

class PlaceWrapper(val place: Place, val bitmap: Bitmap?) : ClusterItem, Serializable {
    init {
        CustomTabUtils.HELPER.mayLaunchUrl(place.websiteUri, null, null)
    }

    override fun getPosition(): LatLng {
        return place.latLng
    }

    override fun getTitle(): String {
        return place.name.toString()
    }

    override fun getSnippet(): String? {
        return null
    }
}