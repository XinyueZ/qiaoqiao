package com.qiaoqiao.repository

import android.app.AlarmManager
import android.content.Context
import android.support.annotation.NonNull
import android.support.v7.preference.PreferenceManager
import com.google.android.gms.maps.model.LatLng
import com.qiaoqiao.repository.annotation.RepositoryScope
import com.qiaoqiao.repository.annotation.target.*
import com.qiaoqiao.repository.backend.Google
import com.qiaoqiao.repository.backend.model.wikipedia.LangLink
import com.qiaoqiao.utils.NetworkUtils
import com.qiaoqiao.utils.NetworkUtils.*

@RepositoryScope
class DsRepository(google: Google,
                   wikipedia: com.qiaoqiao.repository.backend.Wikipedia,
                   @Camera val cameraDs: AbstractDsSource,
                   @Knowledge val knowledgeRemoteDs: AbstractDsSource,
                   @Database val databaseDs: AbstractDsSource,
                   @RemoteImage val remoteImageDs: AbstractDsSource,
                   @LocalImage val localImageDs: AbstractDsSource) : AbstractDsSource(google, wikipedia) {

    override fun onGeosearchQuery(@NonNull latLng: LatLng, radius: Long, @NonNull callback: DsLoadedCallback) {
        knowledgeRemoteDs.onGeosearchQuery(latLng, radius, callback)
    }

    override fun onKnowledgeQuery(pageId: Int, @NonNull callback: DsLoadedCallback) {
        knowledgeRemoteDs.onKnowledgeQuery(pageId, callback)
    }

    override fun onKnowledgeQuery(@NonNull keyword: String, @NonNull callback: DsLoadedCallback) {
        knowledgeRemoteDs.onKnowledgeQuery(keyword, callback)
    }

    override fun onKnowledgeQuery(@NonNull langLink: LangLink, @NonNull callback: DsLoadedCallback) {
        knowledgeRemoteDs.onKnowledgeQuery(langLink, callback)
    }

    override fun onBytes(@NonNull bytes: ByteArray, @NonNull callback: DsLoadedCallback) {
        cameraDs.onBytes(bytes, callback)
    }

    override fun onTranslate(@NonNull q: String, @NonNull callback: DsLoadedCallback) {
        knowledgeRemoteDs.onKnowledgeQuery(q, callback)
    }

    override fun onRecentRequest(@NonNull callback: DsLoadedCallback) {
        databaseDs.onRecentRequest(callback)
    }

    override fun onImage(@NonNull cxt: Context, @NonNull callback: DsLoadedCallback) {
        val goodNetwork = (!NetworkUtils.isOnline(cxt) ||
                NetworkUtils.getCurrentNetworkType(cxt) == CONNECTION_SLOW ||
                NetworkUtils.getCurrentNetworkType(cxt) == CONNECTION_ROAMING ||
                NetworkUtils.getCurrentNetworkType(cxt) == CONNECTION_OFFLINE)
        when {
            goodNetwork ->
                localImageDs.onImage(cxt, callback)
            else -> {
                val prefs = PreferenceManager.getDefaultSharedPreferences(cxt)
                val lastTime = prefs.getLong("remote-launch-image-time", -1)
                if (lastTime < 0 || System.currentTimeMillis() - lastTime > AlarmManager.INTERVAL_FIFTEEN_MINUTES) {
                    remoteImageDs.onImage(cxt, callback)
                } else {
                    localImageDs.onImage(cxt, callback)
                }
            }
        }
    }
}