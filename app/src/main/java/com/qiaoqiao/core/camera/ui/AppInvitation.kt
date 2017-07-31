package com.qiaoqiao.core.camera.ui

import android.app.Activity
import android.content.ActivityNotFoundException
import android.net.Uri
import android.support.v7.app.AlertDialog
import com.google.android.gms.appinvite.AppInviteInvitation
import com.qiaoqiao.R
import com.qiaoqiao.utils.AppUtils

const val REQ_INVITE = 0x56

internal object AppInvitation {
     fun sendAppInvitation(cxt: Activity) {
        val app = cxt.application
        val invitationTitle = app.getString(R.string.invitation_title)
        val invitationMessage = app.getString(R.string.invitation_message)
        val invitationDeepLink = app.getString(R.string.invitation_link)
        val invitationCustomImage = app.getString(R.string.invitation_image)
        val invitationCta = app.getString(R.string.invitation_cta)

        val intent = AppInviteInvitation.IntentBuilder(invitationTitle).setMessage(invitationMessage)
                .setDeepLink(Uri.parse(invitationDeepLink))
                .setCustomImage(Uri.parse(invitationCustomImage))
                .setCallToActionText(invitationCta)
                .build()
        try {
            cxt.startActivityForResult(intent, REQ_INVITE)
        } catch (ex: ActivityNotFoundException) {
            AlertDialog.Builder(cxt).setTitle(R.string.invitation_title)
                    .setMessage(R.string.invitation_error)
                    .setPositiveButton(android.R.string.ok) { dialog, whichButton ->
                        dialog.dismiss()
                        AppUtils.goToPlayServiceDownload(app)
                    }
                    .setCancelable(true)
                    .create()
                    .show()
        } catch (ex: NullPointerException) {
            AlertDialog.Builder(cxt).setTitle(R.string.invitation_title).setMessage(R.string.invitation_error).setPositiveButton(android.R.string.ok) { dialog, whichButton ->
                dialog.dismiss()
                AppUtils.goToPlayServiceDownload(app)
            }.setCancelable(true).create().show()
        }
    }
}