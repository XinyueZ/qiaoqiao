package com.qiaoqiao.core.camera.awareness.map

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Build
import android.support.annotation.DrawableRes
import android.support.v7.content.res.AppCompatResources
import android.view.LayoutInflater
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.clustering.Cluster
import com.google.maps.android.clustering.ClusterItem
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.clustering.view.DefaultClusterRenderer
import com.google.maps.android.ui.IconGenerator
import com.qiaoqiao.R
import com.qiaoqiao.databinding.LayoutPlaceBinding
import com.qiaoqiao.repository.backend.model.wikipedia.geo.Geosearch

class ClusterRenderer internal constructor(
        private val context: Context,
        map: GoogleMap,
        clusterManager: ClusterManager<ClusterItem>,
        inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater,
        private val binding: LayoutPlaceBinding = LayoutPlaceBinding.inflate(inflater)!!
) : DefaultClusterRenderer<ClusterItem>(context, map, clusterManager) {
    init {
        val g = IconGenerator(context)
        g.setContentView(binding.root)
        binding.iconGenerator = g
    }

    override fun shouldRenderAsCluster(cluster: Cluster<ClusterItem>) = cluster.size < 0

    override fun onBeforeClusterItemRendered(clusterItem: ClusterItem?, options: MarkerOptions?) {
        when {
            clusterItem == null || options == null -> return
            else -> when (clusterItem) {
                is Geosearch -> options.position(clusterItem.position)
                        .icon(getBitmapDescriptor(R.drawable.ic_geosearch))
                else -> {
                    binding.image.setImageBitmap((clusterItem as PlaceWrapper).bitmap)
                    options.position(clusterItem.position)
                            .icon(BitmapDescriptorFactory.fromBitmap(binding.iconGenerator?.makeIcon()))
                }
            }
        }
    }

    private fun getBitmapDescriptor(@DrawableRes resId: Int): BitmapDescriptor? {
        when {
            android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP -> return BitmapDescriptorFactory.fromResource(resId)
            else -> {
                val drawable = AppCompatResources.getDrawable(context, resId) ?: return null
                with(drawable) {
                    val canvas = Canvas()
                    val bitmap = Bitmap.createBitmap(this.intrinsicWidth, this.intrinsicHeight, Bitmap.Config.ARGB_8888)
                    canvas.setBitmap(bitmap)
                    this.setBounds(0, 0, this.intrinsicWidth, this.intrinsicHeight)
                    this.draw(canvas)
                    return BitmapDescriptorFactory.fromBitmap(bitmap)
                }
            }
        }
    }
}