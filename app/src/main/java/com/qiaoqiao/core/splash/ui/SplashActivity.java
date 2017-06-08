package com.qiaoqiao.core.splash.ui;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import com.qiaoqiao.R;
import com.qiaoqiao.app.App;
import com.qiaoqiao.core.splash.SplashContract;
import com.qiaoqiao.core.splash.SplashPresenter;
import com.qiaoqiao.utils.SystemUiHelper;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

public final class SplashActivity extends AppCompatActivity implements Runnable {
	/**
	 * Main layout for this component.
	 */
	private static final int LAYOUT = R.layout.activity_splash;
	@Inject @Nullable SplashPresenter mPresenter;
	@Inject @Nullable SplashContract.LaunchImageView mLaunchImageView;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		SystemUiHelper uiHelper = new SystemUiHelper(this, SystemUiHelper.LEVEL_IMMERSIVE, 0);
		uiHelper.hide();
		super.onCreate(savedInstanceState);
		DataBindingUtil.setContentView(this, LAYOUT);

		App.inject(this);
	}

	@Inject
	void injected() {
		new Handler().postDelayed(this, TimeUnit.SECONDS.toMillis(5));
	}

	@Override
	public void run() {
		getSupportFragmentManager().beginTransaction()
		                           .replace(R.id.splash_root_fl, (Fragment) mLaunchImageView)
		                           .commit();
		if (mPresenter != null) {
			mPresenter.begin(this);
		}
	}

	@Override
	protected void onDestroy() {
		if (mPresenter != null) {
			mPresenter.end(this);
		}
		super.onDestroy();
	}
}
