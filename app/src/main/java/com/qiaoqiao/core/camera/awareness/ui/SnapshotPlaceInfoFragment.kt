package com.qiaoqiao.core.camera.awareness.ui

import CustomTabUtils
import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.support.design.widget.BottomSheetDialogFragment
import android.support.v7.app.AppCompatDialogFragment
import android.text.TextUtils
import android.view.View
import com.bumptech.glide.Glide
import com.qiaoqiao.R
import com.qiaoqiao.app.App
import com.qiaoqiao.core.camera.awareness.map.PlaceWrapper
import com.qiaoqiao.core.location.MapActivity
import com.qiaoqiao.databinding.FragmentSnapshotPlaceInfoBinding

private const val LAYOUT = R.layout.fragment_snapshot_place_info
private val EXTRAS_PLACE = SnapshotPlaceInfoFragment::class.java.name + ".EXTRAS.place"

class SnapshotPlaceInfoFragment : BottomSheetDialogFragment(), View.OnClickListener {
    private var behavior: BottomSheetBehavior<*>? = null
    private var binding: FragmentSnapshotPlaceInfoBinding? = null

    companion object {
        fun newInstance(cxt: Context, placeWrapper: PlaceWrapper): AppCompatDialogFragment {
            val args = Bundle(1)
            args.putSerializable(EXTRAS_PLACE, placeWrapper)
            return BottomSheetDialogFragment.instantiate(cxt, SnapshotPlaceInfoFragment::class.java.name, args) as SnapshotPlaceInfoFragment
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        binding = FragmentSnapshotPlaceInfoBinding.bind(View.inflate(context, LAYOUT, null))
        binding?.let {
            dialog.setContentView(it.root)
            behavior = BottomSheetBehavior.from(it.root.parent as View)
            it.placeWrapper = arguments.getSerializable(EXTRAS_PLACE) as PlaceWrapper
            it.clickHandler = this@SnapshotPlaceInfoFragment
        }
        return dialog
    }

    override fun onStart() {
        super.onStart()
        val placeWrapper = arguments.getSerializable(EXTRAS_PLACE) as PlaceWrapper
        CustomTabUtils.warmUp(activity, (placeWrapper.place.websiteUri))
    }

    override fun onStop() {
        super.onStop()
        CustomTabUtils.clean(activity)
    }

    override fun onResume() {
        super.onResume()
        binding?.openMapFl?.isActivated = true
    }

    override fun onClick(v: View?) {
        if (v == null) return
        val placeWrapper = arguments.getSerializable(EXTRAS_PLACE) as PlaceWrapper
        when (v.id) {
            R.id.web_tv -> {
                val app = context.applicationContext as App
                CustomTabUtils.openWeb(activity, placeWrapper.place
                        .websiteUri, app.customTabConfig.builder)
            }
            R.id.tel_tv -> callPhoneNumber(context,
                    placeWrapper.place
                            .phoneNumber
                            .toString())
            else -> MapActivity.showInstance(activity, placeWrapper.position, binding!!.openMapBtn)
        }
    }

    private fun getSanitizedPhoneNumber(phoneNumber: String) = phoneNumber.replace("[^0-9+]+".toRegex(), "")

    private fun createCallPhoneNumberIntent(phoneNumber: String?) = if (phoneNumber != null) {
        val sanitizedPhoneNumber = getSanitizedPhoneNumber(phoneNumber)
        if (!TextUtils.isEmpty(sanitizedPhoneNumber)) {
            Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + sanitizedPhoneNumber))
        } else {
            null
        }
    } else {
        null
    }

    private fun callPhoneNumber(ctx: Context, phoneNumber: String) {
        val intent = createCallPhoneNumberIntent(phoneNumber) ?: return
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        try {
            ctx.startActivity(intent)
        } catch (e: ActivityNotFoundException) {
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding?.let {
            Glide.with(it.placeIv).clear(it.placeIv)
        }
    }
}