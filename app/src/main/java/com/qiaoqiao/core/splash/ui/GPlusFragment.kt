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
import com.google.firebase.auth.FirebaseAuth
import com.qiaoqiao.R
import com.qiaoqiao.app.*
import com.qiaoqiao.databinding.FragmentGplusBinding
import com.qiaoqiao.utils.DeviceUtils

class GPlusFragment : Fragment(), View.OnClickListener {
    private var binding: FragmentGplusBinding? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentGplusBinding.inflate(inflater, container, false)
        binding?.clickHandler = this
        context?.let {
            binding?.peoplePhotoIv?.setImageDrawable(AppCompatResources.getDrawable(it, R.drawable.ic_people))
        }
        return binding?.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding?.let {
            it.root.layoutParams.height = Math.ceil((DeviceUtils.getScreenSize(context).Height * (1 - 0.618f)).toDouble()).toInt()

        }
    }

    private fun signIn() {
        activity?.let {
            ConnectGoogleActivity.showInstance(it, false)
        }
    }

    private fun signOut() {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        val edit = prefs.edit()
        edit.putString(KEY_GOOGLE_ID, DEFAULT_GOOGLE_ID)
        edit.putString(KEY_GOOGLE_PHOTO_URL, DEFAULT_GOOGLE_PHOTO_URL)
        edit.putString(KEY_GOOGLE_DISPLAY_NAME, DEFAULT_GOOGLE_DISPLAY_NAME)
        SharedPreferencesCompat.EditorCompat.getInstance().apply(edit)
        activity?.let {
            ConnectGoogleActivity.showInstance(it, true)
        }
    }

    override fun onResume() {
        showUIStatus()
        super.onResume()
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.btn -> {
                if (FirebaseAuth.getInstance().currentUser == null) {
                    signIn()
                } else {
                    signOut()
                }
            }
        }
    }

    private fun showUIStatus() {
        context?.let {
            it.apply {
                binding?.let {
                    if (FirebaseAuth.getInstance().currentUser == null) {
                        it.btn.setText(R.string.login_google)
                        it.root.setBackgroundResource(R.color.colorBlueGrey)
                        it.peopleNameTv.text = ""
                        it.peoplePhotoIv.setImageDrawable(AppCompatResources.getDrawable(this@apply, R.drawable.ic_people))

                    } else {
                        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
                        it.btn.setText(R.string.logout_google)
                        val name = prefs.getString(KEY_GOOGLE_DISPLAY_NAME, null)
                        val thumbnailUrl = prefs.getString(KEY_GOOGLE_PHOTO_URL, null)
                        it.root.setBackgroundResource(R.color.colorPrimary)
                        if (!TextUtils.isEmpty(thumbnailUrl)) {
                            GlideApp.with(this)
                                    .load(thumbnailUrl)
                                    .error(AppCompatResources.getDrawable(this@apply, R.drawable.ic_people))
                                    .placeholder(AppCompatResources.getDrawable(this@apply, R.drawable.ic_people))
                                    .into(it.peoplePhotoIv)
                        }
                        if (!TextUtils.isEmpty(name)) {
                            it.peopleNameTv.text = name
                        }
                    }
                }
            }
        }
    }
}