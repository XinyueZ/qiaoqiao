package com.qiaoqiao.core.camera.ui

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.view.ViewCompat
import android.view.Menu
import com.qiaoqiao.R
import com.qiaoqiao.databinding.ActivityCameraBinding
import java.lang.ref.WeakReference

internal class Scenario(private val binding: ActivityCameraBinding,
                        snapshotPlacesFragment: Fragment,
                        cropFragment: Fragment) {
    private val snapshotPlacesFragmentRef = WeakReference<Fragment>(snapshotPlacesFragment)
    private val cropFragmentRef = WeakReference<Fragment>(cropFragment)

    fun adjustUIForDifferentFragmentScenario(fragmentManager: FragmentManager, menu: Menu) {
        if (snapshotPlacesFragmentRef.get() == null || cropFragmentRef.get() == null) return

        val isStillshot = true
        val isSnapshotPlacesThere = (snapshotPlacesFragmentRef.get() as Fragment).isAdded
        val isCropThere = (cropFragmentRef.get() as Fragment).isAdded

        binding.navView.menu
                .findItem(R.id.action_places).isVisible = !isSnapshotPlacesThere
        binding.navView.menu
                .findItem(R.id.action_from_local).isVisible = !isSnapshotPlacesThere
        binding.navView.menu
                .findItem(R.id.action_from_web).isVisible = !isSnapshotPlacesThere

        menu.findItem(R.id.action_crop_rotate).isVisible = isCropThere && !isSnapshotPlacesThere
        menu.findItem(R.id.action_video).isVisible = !isCropThere && !isSnapshotPlacesThere && isStillshot
        menu.findItem(R.id.action_photo).isVisible = !isCropThere && !isSnapshotPlacesThere && !isStillshot

        ViewCompat.animate(binding.expandMoreBtn)
                .alpha((if (!isCropThere && !isSnapshotPlacesThere)
                    1
                else
                    0).toFloat())
                .start()
        ViewCompat.animate(binding.expandLessBtn)
                .alpha((if (!isCropThere && !isSnapshotPlacesThere)
                    1
                else
                    0).toFloat())
                .start()

        val view = fragmentManager.findFragmentById(R.id.stackview_history_fg)
                .view
        ViewCompat.animate(view)
                .alpha((if (!isCropThere && !isSnapshotPlacesThere)
                    1
                else
                    0).toFloat())
                .start()
    }

}