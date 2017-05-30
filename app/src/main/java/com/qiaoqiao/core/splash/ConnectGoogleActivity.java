package com.qiaoqiao.core.splash;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.SharedPreferencesCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.qiaoqiao.R;
import com.qiaoqiao.databinding.ActivityConnectGoogleBinding;
import com.qiaoqiao.utils.DeviceUtils;
import com.qiaoqiao.utils.LL;

import static com.qiaoqiao.app.PrefsKeys.KEY_GOOGLE_DISPLAY_NAME;
import static com.qiaoqiao.app.PrefsKeys.KEY_GOOGLE_ID;
import static com.qiaoqiao.app.PrefsKeys.KEY_GOOGLE_PHOTO_URL;

/**
 * Login on Google.
 *
 * @author Xinyue Zhao
 */
public final class ConnectGoogleActivity extends AppCompatActivity implements View.OnClickListener {
	private static final int PLAY_CLIENT_ID = 0x2;
	private static final String EXTRAS_SIGN_OUT = ConnectGoogleActivity.class.getName() + ".EXTRAS.sign.out";
	/**
	 * Main layout for this component.
	 */
	private static final int LAYOUT = R.layout.activity_connect_google;
	/**
	 * Request-id of this  {@link Activity}.
	 */
	public static final int REQ_SIGN_GOOGLE = 0x34;
	/**
	 * Data-binding.
	 */
	private ActivityConnectGoogleBinding mBinding;

	private boolean mVisible;
	/**
	 * The Google-API.
	 */
	private GoogleApiClient mGoogleApiClient;


	public static void showInstance(@NonNull Activity cxt, boolean signOut) {
		Intent intent = new Intent(cxt, ConnectGoogleActivity.class);
		intent.putExtra(EXTRAS_SIGN_OUT, signOut);
		intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
		ActivityCompat.startActivityForResult(cxt, intent, REQ_SIGN_GOOGLE, Bundle.EMPTY);
	}


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mVisible = false;
		mBinding = DataBindingUtil.setContentView(this, LAYOUT);
		mBinding.setClickHandler(this);
		mBinding.appbar.getLayoutParams().height = (int) Math.ceil(DeviceUtils.getScreenSize(this).Height * (1 - 0.618f));
		mBinding.googleLoginBtn.setSize(SignInButton.SIZE_WIDE);
		mBinding.helloTv.setText(R.string.app_description);
		mGoogleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(this /* FragmentActivity */,
		                                                                      PLAY_CLIENT_ID,
		                                                                      connectionResult -> Snackbar.make(mBinding.connectFl, R.string.connect_google_fail, Snackbar.LENGTH_LONG)
		                                                                                                  .setAction(R.string.exit_app, v -> ActivityCompat.finishAffinity(ConnectGoogleActivity.this))
		                                                                                                  .show())
		                                                    .addApi(Auth.GOOGLE_SIGN_IN_API,
		                                                            new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail()
		                                                                                                                                .requestId()
		                                                                                                                                .requestIdToken(getString(R.string.default_web_client_id))
		                                                                                                                                .build())
		                                                    .build();


		if (getIntent().getBooleanExtra(EXTRAS_SIGN_OUT, false)) {
			signOutGoogle();
		}
		mBinding.googleLoginBtn.setOnClickListener(this);
	}

	@Override
	protected void onActivityResult(int requestCode, int responseCode, Intent intent) {
		if (requestCode == REQ_SIGN_GOOGLE) {
			GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(intent);
			handleSignInResult(result);
		}
	}

	private void handleSignInResult(GoogleSignInResult result) {
		if (result.isSuccess()) {
			// Signed in successfully, show authenticated UI.
			GoogleSignInAccount acct = result.getSignInAccount();
			if (!mVisible) {
				SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
				final SharedPreferences.Editor edit = prefs.edit();
				if (acct != null) {
					edit.putString(KEY_GOOGLE_ID, acct.getId());
					edit.putString(KEY_GOOGLE_DISPLAY_NAME, acct.getDisplayName());
					if (acct.getPhotoUrl() != null) {
						Glide.with(getApplicationContext())
						     .load(acct.getPhotoUrl())
						     .into(mBinding.thumbIv);
						edit.putString(KEY_GOOGLE_PHOTO_URL,
						               acct.getPhotoUrl()
						                   .toString());
					}
					SharedPreferencesCompat.EditorCompat.getInstance()
					                                    .apply(edit);
					mBinding.thumbIv.animate()
					                .cancel();
					mBinding.thumbIv.animate()
					                .alpha(1)
					                .setDuration(500)
					                .start();


					mBinding.helloTv.setText(getString(android.R.string.ok, acct.getDisplayName()));
					mBinding.loginPb.setVisibility(View.GONE);
					mBinding.closeBtn.setVisibility(View.VISIBLE);
					Animation shake = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shake);
					mBinding.closeBtn.startAnimation(shake);
				}
			}
			firebaseAuthWithGoogle(acct);
		} else {
			mBinding.helloTv.setText(R.string.app_description);
			mBinding.loginPb.setVisibility(View.GONE);
			mBinding.thumbIv.animate()
			                .cancel();
			mBinding.thumbIv.animate()
			                .alpha(1)
			                .setDuration(500)
			                .start();
			mBinding.googleLoginBtn.setVisibility(View.VISIBLE);
		}
	}


	/**
	 * Sign with Google.
	 */
	private void signInGoogle() {
		mBinding.googleLoginBtn.setVisibility(View.GONE);
		mBinding.loginPb.setVisibility(View.VISIBLE);
		mBinding.helloTv.setText(R.string.connect_google);
		mBinding.thumbIv.animate()
		                .cancel();
		mBinding.thumbIv.animate()
		                .alpha(0.3f)
		                .setDuration(500)
		                .start();
		startActivityForResult(Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient), REQ_SIGN_GOOGLE);
	}

	private void signOutGoogle() {
		Auth.GoogleSignInApi.signOut(mGoogleApiClient);
	}


	private void close() {
		setResult(RESULT_OK);
		ActivityCompat.finishAfterTransition(ConnectGoogleActivity.this);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mVisible = true;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.google_login_btn:
				signInGoogle();
				break;
			case R.id.close_btn:
				close();
				break;
		}
	}

	private void firebaseAuthWithGoogle(@NonNull GoogleSignInAccount acct) {
		AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
		FirebaseAuth.getInstance()
		            .signInWithCredential(credential)
		            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
			            @Override
			            public void onComplete(@NonNull Task<AuthResult> task) {
				            if (!task.isSuccessful()) {
					            LL.d("Firebase is ready.");
				            }
			            }
		            });
	}
}
