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
            return
        }
        Flowable.just(response.responses[0]).compose(Composer()).flatMap {
            val list = arrayListOf<VisionEntity>()
            it.labelAnnotations.forEach({
                if (!TextUtils.isEmpty(it.description) && it.score > Confidence.getValueOnly(contextReference.get() as Context,
                        KEY_CONFIDENCE_LABEL,
                        DEFAULT_CONFIDENCE_LABEL)) {
                    list.add(VisionEntity(it, "LABEL_DETECTION").setActivated(true))
                }
            })
            it.landmarkAnnotations.forEach({
                if (!TextUtils.isEmpty(it.description) && it.score > Confidence.getValueOnly(contextReference.get() as Context,
                        KEY_CONFIDENCE_IMAGE,
                        DEFAULT_CONFIDENCE_IMAGE)) {
                    list.add(VisionEntity(it, "LANDMARK_DETECTION").setActivated(true))
                }
            })
            it.logoAnnotations.forEach({
                if (!TextUtils.isEmpty(it.description) && it.score > Confidence.getValueOnly(contextReference.get() as Context, KEY_CONFIDENCE_LOGO,
                        DEFAULT_CONFIDENCE_LOGO)) {
                    list.add(VisionEntity(it, "LOGO_DETECTION").setActivated(true))
                }
            })
            it.webDetection.webEntities.forEach({
                if (!TextUtils.isEmpty(it.description) && it.score > Confidence.createFromPrefs(contextReference.get() as Context, KEY_CONFIDENCE_IMAGE, DEFAULT_CONFIDENCE_IMAGE)
                        .value) {
                    list.add(VisionEntity(it, "WEB_DETECTION").setActivated(true))
                }
            })
            Flowable.just(list)
        }.subscribe({ t -> view.addEntities(t) })
    }
}