package com.qiaoqiao.core.camera.ui

import android.support.v4.view.ViewCompat
import android.view.View
import com.qiaoqiao.R
import net.frakbot.glowpadbackport.GlowPadView

abstract class SimpleOnTriggerListener : GlowPadView.OnTriggerListener {
    final override fun onFinishFinalAnimation() {
    }

    final override fun onReleased(p0: View?, p1: Int) {
    }

    final override fun onGrabbedStateChange(p0: View?, p1: Int) {
    }

    final override fun onGrabbed(p0: View?, p1: Int) {
    }

    override fun onTrigger(p0: View?, p1: Int) {
        ViewCompat.setTransitionName(p0, p0?.context?.getString(R.string.transition_share_item_name))
    }
}