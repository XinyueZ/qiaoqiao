package com.qiaoqiao.home.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;

import com.qiaoqiao.R;
import com.qiaoqiao.app.App;
import com.qiaoqiao.databinding.HomeBinding;
import com.qiaoqiao.ds.web.ui.FromInputWebLinkFragment;
import com.qiaoqiao.home.DaggerHomeComponent;
import com.qiaoqiao.home.Home;
import com.qiaoqiao.home.HomeContract;
import com.qiaoqiao.home.HomeModule;
import com.qiaoqiao.utils.SystemUiHelper;

import javax.inject.Inject;

import static android.os.Bundle.EMPTY;

public final class HomeActivity extends AppCompatActivity implements HomeContract.View,
                                                                     View.OnClickListener {
	private static final int LAYOUT = R.layout.activity_home;
	private static final int REQUEST_FILE_SELECTOR = 0x19;
	private @Nullable Snackbar mSnackbar;
	@Inject Home mHome;

	/**
	 * Show single instance of {@link HomeActivity}
	 *
	 * @param cxt {@link Activity}.
	 */
	public static void showInstance(@NonNull Activity cxt) {
		Intent intent = new Intent(cxt, HomeActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
		ActivityCompat.startActivity(cxt, intent, EMPTY);
	}


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SystemUiHelper uiHelper = new SystemUiHelper(this, SystemUiHelper.LEVEL_IMMERSIVE, 0);
		uiHelper.hide();
		HomeBinding binding = DataBindingUtil.setContentView(this, LAYOUT);
		binding.setUiHelper(uiHelper);
		binding.setDecorView((ViewGroup) getWindow().getDecorView());


		DaggerHomeComponent.builder()
		                   .dsRepositoryComponent(((App) getApplication()).getRepositoryComponent())
		                   .homeModule(new HomeModule(this, binding))
		                   .build()
		                   .injectHome(this);
	}


	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		mHome.changeFocus();
		super.onWindowFocusChanged(hasFocus);
	}

	@SuppressLint("RestrictedApi")
	@Override
	public void showInputFromWeb() {
		getSupportFragmentManager().beginTransaction()
		                           .setCustomAnimations(R.anim.slide_in_from_right, R.anim.slide_out_to_left, android.R.anim.slide_in_left, android.R.anim.slide_out_right)
		                           .replace(R.id.content_root_fl, FromInputWebLinkFragment.newInstance(this))
		                           .addToBackStack(null)
		                           .commit();
	}

	@Override
	public void showLoadFromLocal() {
		Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		startActivityForResult(galleryIntent, REQUEST_FILE_SELECTOR);
	}

	@Override
	protected void onResume() {
		super.onResume();
		mHome.start();
	}

	@Override
	protected void onPause() {
		mHome.stop();
		super.onPause();
	}

	@Override
	public void setPresenter(@NonNull Home presenter) {
		mHome = presenter;
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
			case REQUEST_FILE_SELECTOR:
				if (!(resultCode == Activity.RESULT_OK && data != null && data.getData() != null)) {
					super.onActivityResult(requestCode, resultCode, data);
					return;
				}
				mHome.openLocal(getApplicationContext(), data.getData());
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void showError(@NonNull android.view.View view, @NonNull String errorMessage) {
		mSnackbar = Snackbar.make(view, errorMessage, Snackbar.LENGTH_LONG)
		                    .setAction(android.R.string.ok, this);
		mSnackbar.show();
	}

	@Override
	public void onClick(View v) {
		if (mSnackbar == null) {
			return;
		}
		mSnackbar.dismiss();
	}
}
