package com.qiaoqiao.core.splash.ui

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.SharedPreferencesCompat
import android.support.v7.content.res.AppCompatResources
import android.support.v7.preference.PreferenceManager
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.qiaoqiao.R
import com.qiaoqiao.app.PrefsKeys
import com.qiaoqiao.app.PrefsKeys.*
import com.qiaoqiao.databinding.FragmentGplusBinding
import com.qiaoqiao.utils.DeviceUtils

class GPlusFragment : Fragment(), View.OnClickListener {
    private var binding: FragmentGplusBinding? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentGplusBinding.inflate(inflater, container, false)
        binding?.clickHandler = this
        binding?.peoplePhotoIv?.setImageDrawable(AppCompatResources.getDrawable(context, R.drawable.ic_people))
        return binding?.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding?.let {
            it.root.layoutParams.height = Math.ceil((DeviceUtils.getScreenSize(context).Height * (1 - 0.618f)).toDouble()).toInt()

        }
    }

    private fun signIn() {
        ConnectGoogleActivity.showInstance(activity, false)
    }

    private fun signOut() {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        val edit = prefs.edit()
        edit.putString(KEY_GOOGLE_ID, DEFAULT_GOOGLE_ID)
        edit.putString(KEY_GOOGLE_PHOTO_URL, DEFAULT_GOOGLE_PHOTO_URL)
        edit.putString(KEY_GOOGLE_DISPLAY_NAME, DEFAULT_GOOGLE_DISPLAY_NAME)
        SharedPreferencesCompat.EditorCompat.getInstance().apply(edit)
        ConnectGoogleActivity.showInstance(activity, true)
    }

    override fun onResume() {
        showUIStatus()
        super.onResume()
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.btn -> {
                val prefs = PreferenceManager.getDefaultSharedPreferences(context)
                if (TextUtils.isEmpty(prefs.getString(PrefsKeys.KEY_GOOGLE_ID, DEFAULT_GOOGLE_ID))) {
                    signIn()
                } else {
                    signOut()
                }
            }
        }
    }

    private fun showUIStatus() {
        binding?.let {
            val prefs = PreferenceManager.getDefaultSharedPreferences(context)
            if (TextUtils.isEmpty(prefs.getString(PrefsKeys.KEY_GOOGLE_ID, DEFAULT_GOOGLE_ID))) {
                it.btn.setText(R.string.login_google)
                it.root.setBackgroundResource(R.color.colorBlueGrey)
                it.peopleNameTv.text = ""
                it.peoplePhotoIv.setImageDrawable(AppCompatResources.getDrawable(context, R.drawable.ic_people))

            } else {
                it.btn.setText(R.string.logout_google)
                val name = prefs.getString(PrefsKeys.KEY_GOOGLE_DISPLAY_NAME, null)
                val thumbnailUrl = prefs.getString(KEY_GOOGLE_PHOTO_URL, null)
                it.root.setBackgroundResource(R.color.colorPrimary)
                if (!TextUtils.isEmpty(thumbnailUrl)) {
                    Glide.with(this)
                            .load(thumbnailUrl)
                            .error(AppCompatResources.getDrawable(context, R.drawable.ic_people))
                            .placeholder(AppCompatResources.getDrawable(context, R.drawable.ic_people))
                            .into(it.peoplePhotoIv)
                }
                if (!TextUtils.isEmpty(name)) {
                    it.peopleNameTv.text = name
                }
            }
        }
    }
}