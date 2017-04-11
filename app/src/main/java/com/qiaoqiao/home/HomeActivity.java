package com.qiaoqiao.home;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.qiaoqiao.R;
import com.qiaoqiao.app.App;
import com.qiaoqiao.databinding.HomeBinding;
import com.qiaoqiao.utils.SystemUiHelper;

import javax.inject.Inject;

public final class HomeActivity extends AppCompatActivity {
	private static final int LAYOUT = R.layout.activity_home;
	private HomeBinding mBinding;
	@Inject Home mHome;

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
