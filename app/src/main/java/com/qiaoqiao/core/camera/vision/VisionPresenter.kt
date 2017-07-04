package com.qiaoqiao.core.camera.vision

import android.content.Context
import android.text.TextUtils
import com.google.api.services.vision.v1.model.BatchAnnotateImagesResponse
import com.qiaoqiao.app.PrefsKeys.*
import com.qiaoqiao.core.camera.vision.bus.VisionEntityClickEvent
import com.qiaoqiao.core.camera.vision.model.VisionEntity
import com.qiaoqiao.core.confidence.ui.Confidence
import com.qiaoqiao.repository.DsRepository
import com.qiaoqiao.rx.Composer
import com.qiaoqiao.rx.safeList
import de.greenrobot.event.Subscribe
import io.reactivex.Flowable
import java.lang.ref.Reference
import java.lang.ref.WeakReference
import javax.inject.Inject

class VisionPresenter @Inject constructor(cxt: Context, val view: VisionContract.View, dsRepository: DsRepository) : VisionContract.Presenter(dsRepository) {
    private val contextReference: Reference<Context> = WeakReference<Context>(cxt)

    @Inject
    fun onInjected() {
        view.setPresenter(this)
    }

    @Subscribe
    fun onEvent(e: VisionEntityClickEvent) {
        view.showDetail(e.entity, e.transitionView)

    }

    override fun setRefreshing(refresh: Boolean) {
        view.setRefreshing(refresh)
    }

    override fun clear() {
        view.clear()
    }

    override fun addResponseToScreen(response: BatchAnnotateImagesResponse) {
        if (contextReference.get() == null || response.responses == null || response.responses.isEmpty() || response.responses[0] == null) {
            view.addEntities(arrayListOf<VisionEntity>())
        } else {
            Flowable.merge(
                    Flowable.fromIterable(safeList(response.responses[0].labelAnnotations)).filter({
                        !TextUtils.isEmpty(it.description) && it.score > Confidence.getValueOnly(contextReference.get() as Context,
                                KEY_CONFIDENCE_LABEL,
                                DEFAULT_CONFIDENCE_LABEL)
                    }).map({ VisionEntity(it, "LABEL_DETECTION").setActivated(true) }),
                    Flowable.fromIterable(safeList(response.responses[0].landmarkAnnotations)).filter({
                        !TextUtils.isEmpty(it.description) && it.score > Confidence.getValueOnly(contextReference.get() as Context,
                                KEY_CONFIDENCE_IMAGE,
                                DEFAULT_CONFIDENCE_IMAGE)
                    }).map({ VisionEntity(it, "LANDMARK_DETECTION").setActivated(true) }),
                    Flowable.fromIterable(safeList(response.responses[0].logoAnnotations)).filter({
                        !TextUtils.isEmpty(it.description) && it.score > Confidence.getValueOnly(contextReference.get() as Context, KEY_CONFIDENCE_LOGO,
                                DEFAULT_CONFIDENCE_LOGO)
                    }).map({ VisionEntity(it, "LOGO_DETECTION").setActivated(true) }),
                    Flowable.fromIterable(safeList(response.responses[0].webDetection.webEntities)).filter({
                        !TextUtils.isEmpty(it.description) && it.score > Confidence.createFromPrefs(contextReference.get() as Context, KEY_CONFIDENCE_IMAGE, DEFAULT_CONFIDENCE_IMAGE)
                                .value
                    }).map({ VisionEntity(it, "WEB_DETECTION").setActivated(true) })
            ).compose(Composer()).toList().subscribe({ it -> view.addEntities(it) })
        }
    }
}