package com.qiaoqiao.home;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import com.qiaoqiao.R;
import com.qiaoqiao.app.App;
import com.qiaoqiao.databinding.HomeBinding;
import com.qiaoqiao.utils.SystemUiHelper;

import javax.inject.Inject;

import static android.os.Build.VERSION_CODES.JELLY_BEAN;
import static android.os.Bundle.EMPTY;

public final class HomeActivity extends AppCompatActivity {
	private static final int LAYOUT = R.layout.activity_home;
	private HomeBinding mBinding;
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
		mBinding = DataBindingUtil.setContentView(this, LAYOUT);
		mBinding.setUiHelper(uiHelper);
		DaggerHomeComponent.builder()
		                   .appComponent(App.appComponent)
		                   .homeModule(new HomeModule(mBinding))
		                   .build()
		                   .inject(this);

		initActivity();
	}

	private void initActivity() {
		getWindow().getDecorView()
		           .getViewTreeObserver()
		           .addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
			           @Override
			           public void onGlobalLayout() {
				           ViewGroup decorView = (ViewGroup) getWindow().getDecorView();
				           if (Build.VERSION.SDK_INT >= JELLY_BEAN) {
					           decorView.getViewTreeObserver()
					                    .removeOnGlobalLayoutListener(this);
				           } else {
					           decorView.getViewTreeObserver()
					                    .removeGlobalOnLayoutListener(this);
				           }
				           Rect rect = new Rect();
				           decorView.getWindowVisibleDisplayFrame(rect);
				           mBinding.home.setPadding(0, rect.top, 0, 0);
			           }
		           });

		int resourceId = getResources().getIdentifier("navigation_bar_height", "dimen", "android");
		if (resourceId > 0) {
			((ViewGroup.MarginLayoutParams) mBinding.mainControl.getLayoutParams()).bottomMargin = getResources().getDimensionPixelSize(resourceId);
		}
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		mBinding.getUiHelper()
		        .hide();
	}


	@Override
	protected void onResume() {
		super.onResume();
		mBinding.camera.start();
	}

	@Override
	protected void onPause() {
		mBinding.camera.stop();
		super.onPause();
	}
}
