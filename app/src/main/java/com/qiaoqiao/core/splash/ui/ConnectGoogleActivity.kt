package com.qiaoqiao.core.splash.ui

import android.app.Activity
import android.content.Intent
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.graphics.drawable.Animatable
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.ActivityCompat
import android.support.v4.content.SharedPreferencesCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.content.res.AppCompatResources
import android.support.v7.preference.PreferenceManager
import android.view.View
import android.view.animation.AnimationUtils
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.auth.api.signin.GoogleSignInResult
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.qiaoqiao.R
import com.qiaoqiao.app.GlideApp
import com.qiaoqiao.app.KEY_GOOGLE_DISPLAY_NAME
import com.qiaoqiao.app.KEY_GOOGLE_ID
import com.qiaoqiao.app.KEY_GOOGLE_PHOTO_URL
import com.qiaoqiao.databinding.ActivityConnectGoogleBinding
import com.qiaoqiao.utils.DeviceUtils
import com.qiaoqiao.utils.LL
import com.qiaoqiao.utils.SystemUiHelper

private const val PLAY_CLIENT_ID = 0x2
private const val LAYOUT = R.layout.activity_connect_google
private const val REQ_SIGN_GOOGLE = 0x34

class ConnectGoogleActivity : AppCompatActivity(), View.OnClickListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        OnCompleteListener<AuthResult> {
    private var binding: ActivityConnectGoogleBinding? = null
    private var apiClient: GoogleApiClient? = null
    private var invisible: Boolean = false

    internal companion object {
        private val EXTRAS_SIGN_OUT = ConnectGoogleActivity::class.java.name + ".EXTRAS.sign.out"
        fun showInstance(cxt: Activity, signOut: Boolean) {
            val intent = Intent(cxt, ConnectGoogleActivity::class.java)
            intent.putExtra(EXTRAS_SIGN_OUT, signOut)
            intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
            ActivityCompat.startActivityForResult(cxt, intent, REQ_SIGN_GOOGLE, Bundle.EMPTY)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        val uiHelper = SystemUiHelper(this, SystemUiHelper.LEVEL_IMMERSIVE, 0)
        uiHelper.hide()
        super.onCreate(savedInstanceState)
        invisible = false

        binding = DataBindingUtil.setContentView<ViewDataBinding>(this, LAYOUT) as ActivityConnectGoogleBinding?
        binding?.let {
            it.clickHandler = this
            it.uiHelper = uiHelper
            it.thumbIv.setImageDrawable(AppCompatResources.getDrawable(applicationContext, R.drawable.ic_people))
            it.appbar.layoutParams.height = Math.ceil((DeviceUtils.getScreenSize(this).Height * (1 - 0.618f)).toDouble()).toInt()
            it.googleLoginBtn.setSize(SignInButton.SIZE_WIDE)
            it.helloTv.setText(R.string.app_description)
            apiClient = GoogleApiClient.Builder(this).enableAutoManage(this /* FragmentActivity */, PLAY_CLIENT_ID, this)
                    .addConnectionCallbacks(this)
                    .addApi(Auth.GOOGLE_SIGN_IN_API,
                            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail()
                                    .requestId()
                                    .requestIdToken(getString(R.string.web_client_id))
                                    .build())
                    .build()
            it.googleLoginBtn.setOnClickListener(this)
        }
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        binding?.uiHelper?.hide()
        super.onWindowFocusChanged(hasFocus)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            REQ_SIGN_GOOGLE -> {
                val res = Auth.GoogleSignInApi.getSignInResultFromIntent(data)
                if (res != null)
                    handleSignInResult(res)
            }
            else -> super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun handleSignInResult(result: GoogleSignInResult) {
        binding?.let {
            if (result.isSuccess) {
                // Signed in successfully, show authenticated UI.
                val acct = result.signInAccount
                if (!invisible) {
                    val prefs = PreferenceManager.getDefaultSharedPreferences(applicationContext)
                    val edit = prefs.edit()
                    if (acct != null) {
                        edit.putString(KEY_GOOGLE_ID, acct.id)
                        edit.putString(KEY_GOOGLE_DISPLAY_NAME, acct.displayName)
                        if (acct.photoUrl != null) {
                            GlideApp.with(applicationContext)
                                    .load(acct.photoUrl)
                                    .error(AppCompatResources.getDrawable(applicationContext, R.drawable.ic_people))
                                    .placeholder(AppCompatResources.getDrawable(applicationContext, R.drawable.ic_people))
                                    .into(it.thumbIv)
                            edit.putString(KEY_GOOGLE_PHOTO_URL,
                                    acct.photoUrl.toString())
                        }
                        SharedPreferencesCompat.EditorCompat.getInstance()
                                .apply(edit)

                        it.helloTv.text = getString(R.string.connect_successfully, acct.displayName)
                        firebaseAuthWithGoogle(acct)
                    }
                }
            } else {
                it.helloTv.setText(R.string.app_description)
                it.loginPb.visibility = View.GONE
                it.googleLoginBtn.visibility = View.VISIBLE
                it.skipBtn.visibility = View.VISIBLE
            }
            val drawable = it.loginPb.drawable
            if (drawable is Animatable) {
                (drawable as Animatable).stop()
            }
        }
    }

    private fun signInGoogle() {
        binding?.let {
            it.googleLoginBtn.visibility = View.GONE
            it.loginPb.visibility = View.VISIBLE
            if (it.loginPb.drawable is Animatable) {
                (it.loginPb.drawable as Animatable).start()
            }
            it.skipBtn.visibility = View.GONE
            it.helloTv.setText(R.string.connect_google)
            startActivityForResult(Auth.GoogleSignInApi.getSignInIntent(apiClient), REQ_SIGN_GOOGLE)
        }
    }

    private fun signOutGoogle() {
        Auth.GoogleSignInApi.signOut(apiClient)
    }

    private fun close() {
        setResult(Activity.RESULT_OK)
        supportFinishAfterTransition()
    }

    override fun onDestroy() {
        super.onDestroy()
        invisible = true
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.google_login_btn -> signInGoogle()
            R.id.close_btn -> close()
            R.id.skip_btn -> supportFinishAfterTransition()
        }
    }

    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
        FirebaseAuth.getInstance()
                .signInWithCredential(GoogleAuthProvider.getCredential(acct.idToken, null))
                .addOnCompleteListener(this, this)
    }

    override fun onComplete(task: Task<AuthResult>) {
        if (task.isSuccessful) {
            LL.d("Firebase is ready.")

            binding?.let {
                it.loginPb.visibility = View.GONE
                it.closeBtn.visibility = View.VISIBLE
                val shake = AnimationUtils.loadAnimation(applicationContext, R.anim.shake)
                it.closeBtn.startAnimation(shake)
            }
        }
    }

    override fun onConnectionFailed(connectionResult: ConnectionResult) {
        binding?.let {
            Snackbar.make(it.connectFl, R.string.connect_google_fail, Snackbar.LENGTH_LONG)
                    .setAction(R.string.exit_app, {
                        ActivityCompat.finishAffinity(
                                this@ConnectGoogleActivity)
                    })
                    .show()
        }
    }

    override fun onConnected(p0: Bundle?) {
        if (shouldSignOut()) {
            signOutGoogle()
        }
        binding?.googleLoginBtn?.visibility = View.VISIBLE
    }

    private fun shouldSignOut(): Boolean {
        return intent.getBooleanExtra(EXTRAS_SIGN_OUT, false)
    }

    override fun onConnectionSuspended(i: Int) {

    }
}