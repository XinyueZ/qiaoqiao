package com.qiaoqiao.home;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.qiaoqiao.R;
import com.qiaoqiao.databinding.ActivityMainBinding;
import com.qiaoqiao.utils.SystemUiHelper;

public final class MainActivity extends AppCompatActivity {
	private static final int LAYOUT = R.layout.activity_main;
	private ActivityMainBinding mBinding;
	private SystemUiHelper mSystemUiHelper;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mSystemUiHelper = new SystemUiHelper(this, SystemUiHelper.LEVEL_IMMERSIVE, 0);
		mSystemUiHelper.hide();
		mBinding = DataBindingUtil.setContentView(this, LAYOUT);
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		mSystemUiHelper.hide();
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
