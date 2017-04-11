package com.qiaoqiao.home.ui;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;

import com.qiaoqiao.R;
import com.qiaoqiao.databinding.HomeBinding;
import com.qiaoqiao.home.DaggerHomeComponent;
import com.qiaoqiao.home.Home;
import com.qiaoqiao.home.HomeContract;
import com.qiaoqiao.home.HomeModule;
import com.qiaoqiao.utils.SystemUiHelper;

import javax.inject.Inject;

import static android.os.Bundle.EMPTY;

public final class HomeActivity extends AppCompatActivity implements HomeContract.View {
	private static final int LAYOUT = R.layout.activity_home;
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
		                   .homeModule(new HomeModule(this, binding))
		                   .build()
		                   .injectHome(this);
	}


	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		mHome.changeFocus();
		super.onWindowFocusChanged(hasFocus);
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
}
