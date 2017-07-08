package com.qiaoqiao.core.camera.awareness

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.os.Looper
import android.support.v4.app.FragmentActivity
import android.support.v7.preference.PreferenceManager
import com.google.android.gms.awareness.Awareness
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.location.places.Places
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem
import com.qiaoqiao.app.PrefsKeys
import com.qiaoqiao.core.camera.awareness.map.PlaceWrapper
import com.qiaoqiao.core.camera.awareness.ui.Adjust
import com.qiaoqiao.repository.DsLoadedCallback
import com.qiaoqiao.repository.DsRepository
import com.qiaoqiao.repository.backend.model.wikipedia.geo.GeoResult
import com.qiaoqiao.rx.Composer
import io.reactivex.Flowable
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

private const val PLAY_CLIENT_ID = 0x3
const val REQ_SETTING_LOCATING = 0x78

class AwarenessPresenter
@Inject
constructor(private val view: AwarenessContract.View, private val apiBuilder: GoogleApiClient.Builder, private val dsRepository: DsRepository) : AwarenessContract.Presenter, GoogleApiClient.OnConnectionFailedListener {
    private var apiClient: GoogleApiClient? = null
    private var localClient: FusedLocationProviderClient? = null
    private val localReq: LocationRequest = LocationRequest.create()

    @Inject
    fun onInjected() {
        view.setPresenter(this)
    }

    override fun begin(hostActivity: FragmentActivity) {
        apiClient = if (apiClient == null) apiBuilder.enableAutoManage(hostActivity, PLAY_CLIENT_ID, this).build() else apiClient
        localClient = LocationServices.getFusedLocationProviderClient(hostActivity.applicationContext)
    }

    override fun end(hostActivity: FragmentActivity) {
        apiClient?.stopAutoManage(hostActivity)
        apiClient?.let {
            if (it.isConnected)
                it.disconnect()
        }
        apiClient = null
    }

    override fun settingLocating(cxt: Activity) {
        LocationServices.getSettingsClient(cxt)
                .checkLocationSettings(LocationSettingsRequest.Builder().setAlwaysShow(true).setNeedBle(true).addLocationRequest(localReq).build())
                .addOnSuccessListener { view.locating() }
                .addOnFailureListener {
                    val exp = it as ApiException
                    when (exp.statusCode) {
                        CommonStatusCodes.RESOLUTION_REQUIRED -> {
                            val resolvable = exp as ResolvableApiException
                            resolvable.startResolutionForResult(cxt,
                                    REQ_SETTING_LOCATING)
                        }
                        LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {
                            view.onLocatingError()
                        }
                    }
                }

    }

    override fun onConnectionFailed(p0: ConnectionResult) {
        view.onPlayServiceConnectionFailed()
    }

    @SuppressLint("MissingPermission")
    override fun locating(cxt: Context) {
        localClient?.requestLocationUpdates(localReq, object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                super.onLocationResult(locationResult)
                locationResult?.let {
                    view.onLocated(LatLng(it.lastLocation.latitude, it.lastLocation.longitude))
                }
            }
        }, Looper.myLooper())
    }

    override fun loadGeosearchAdjust(cxt: Context) {
        view.showAdjust(Adjust.Factory.createAdjust(cxt, PrefsKeys.KEY_GEOSEARCH_RADIUS, PreferenceManager.getDefaultSharedPreferences(cxt)
                .getInt(PrefsKeys.KEY_GEOSEARCH_RADIUS, PrefsKeys.DEFAULT_GEOSEARCH_RADIUS)))
    }

    @SuppressLint("MissingPermission")
    override fun searchAndSearch(cxt: Context, latLng: LatLng) {
        dsRepository.onGeosearchQuery(latLng,
                PreferenceManager.getDefaultSharedPreferences(cxt).getInt(PrefsKeys.KEY_GEOSEARCH_RADIUS, PrefsKeys.DEFAULT_GEOSEARCH_RADIUS).toLong(),
                object : DsLoadedCallback() {
                    override fun onGeosearchResponse(result: GeoResult) {
                        super.onGeosearchResponse(result)
                        Flowable.merge(
                                Flowable.just(Awareness.SnapshotApi.getPlaces(apiClient)).subscribeOn(Schedulers.io())
                                        .map { it.await() }.filter { it.status.isSuccess }.flatMapIterable { it.placeLikelihoods }.filter { apiClient != null }
                                        .map {
                                            val res = Places.GeoDataApi.getPlacePhotos(apiClient, it.place.id).await()
                                            if (res.status.isSuccess && res.photoMetadata.count > 0) {
                                                val photo = res.photoMetadata[0]
                                                val image = photo.getScaledPhoto(apiClient, photo.maxWidth, photo.maxHeight).await().bitmap
                                                res.photoMetadata.release()
                                                PlaceWrapper(it.place, image)
                                            } else PlaceWrapper(it.place, null)
                                        }.filter { it.bitmap != null }.map { it as ClusterItem },
                                Flowable.just(result).subscribeOn(Schedulers.io()).filter { it.query != null }.map { it.query.geosearches }.flatMapIterable { it }
                                        .map { it as ClusterItem }
                        ).compose(Composer()).toList().subscribe(Consumer { view.showAllGeoAndPlaces(it) })
                    }
                })
    }
}