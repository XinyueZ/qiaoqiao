package com.qiaoqiao.detail.ui;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;

import com.qiaoqiao.R;
import com.qiaoqiao.app.App;
import com.qiaoqiao.detail.DaggerDetailComponent;
import com.qiaoqiao.detail.DetailContract;
import com.qiaoqiao.detail.DetailModule;
import com.qiaoqiao.detail.DetailPresenter;

import javax.inject.Inject;

import static android.os.Bundle.EMPTY;


public final class DetailActivity extends AppCompatActivity {
	static final String EXTRAS_KEYWORD = DetailActivity.class.getName() + ".EXTRAS.keyword";
	private static final int LAYOUT = R.layout.activity_detail;
	@Inject DetailPresenter mPresenter;

	public static void showInstance(@NonNull Activity cxt, @NonNull String keyword) {
		Intent intent = new Intent(cxt, DetailActivity.class);
		intent.putExtra(EXTRAS_KEYWORD, keyword);
		intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
		ActivityCompat.startActivity(cxt, intent, EMPTY);
	}

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		DataBindingUtil.setContentView(this, LAYOUT);
		DaggerDetailComponent.builder()
		                     .dsRepositoryComponent(((App) getApplication()).getRepositoryComponent())
		                     .detailModule(new DetailModule((DetailContract.View) getSupportFragmentManager().findFragmentById(R.id.detail_fg), getIntent().getStringExtra(EXTRAS_KEYWORD)))
		                     .build()
		                     .injectDetail(this);
	}
}
