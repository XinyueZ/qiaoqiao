package com.qiaoqiao.core.camera.history

import android.support.v4.app.FragmentActivity
import com.google.api.client.json.gson.GsonFactory
import com.google.api.services.vision.v1.model.BatchAnnotateImagesResponse
import com.qiaoqiao.app.App
import com.qiaoqiao.core.camera.history.bus.HistoryItemClickEvent
import com.qiaoqiao.core.camera.vision.VisionPresenter
import com.qiaoqiao.repository.database.HistoryItem
import de.greenrobot.event.EventBus
import de.greenrobot.event.Subscribe
import io.realm.RealmChangeListener
import io.realm.RealmResults
import javax.inject.Inject

class HistoryPresenter2 @Inject constructor(private val view: HistoryContract.View2) : HistoryContract.Presenter2 {
    var visionPresenter: VisionPresenter? = null
        set
    private var result: RealmResults<HistoryItem>? = null
    private val changeListener = RealmChangeListener<RealmResults<HistoryItem>> { view.updateList(it) }

    @Subscribe
    fun onEvent(e: HistoryItemClickEvent) {
        visionPresenter?.addResponseToScreen(GsonFactory.getDefaultInstance()
                .createJsonParser(e.historyItem?.jsonText)
                .parse(BatchAnnotateImagesResponse::class.java), true)
    }

    @Inject
    fun onInjected() {
        view.setPresenter(this)
    }

    override fun begin(hostActivity: FragmentActivity) {
        EventBus.getDefault()
                .register(this)
        loadHistory()
    }

    override fun end(hostActivity: FragmentActivity) {
        EventBus.getDefault()
                .unregister(this)
        result?.removeChangeListener(changeListener)
    }

    override fun loadHistory() {
        App.realm
                .where(HistoryItem::class.java)
                .findAllAsync()?.let {
            view.showList(it)
            it.addChangeListener(changeListener)
        }
    }
}