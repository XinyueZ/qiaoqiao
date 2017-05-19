package com.qiaoqiao.core.detail.ui;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;

import com.qiaoqiao.R;
import com.qiaoqiao.app.App;
import com.qiaoqiao.core.detail.DetailPresenter;

import javax.inject.Inject;


public final class DetailActivity extends AppCompatActivity {
	static final String EXTRAS_KEYWORD = DetailActivity.class.getName() + ".EXTRAS.keyword";
	private static final int LAYOUT = R.layout.activity_detail;
	@Inject DetailPresenter mPresenter;


	public static void showInstance(@NonNull Activity cxt, @NonNull String keyword, @NonNull View transitionView) {
		String transitionSharedItemName = ViewCompat.getTransitionName(transitionView);
		Intent intent = new Intent(cxt, DetailActivity.class);
		intent.putExtra(EXTRAS_KEYWORD, keyword);
		intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.putExtra(cxt.getString(R.string.transition_share_item_name), transitionSharedItemName);
		ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(cxt, transitionView, transitionSharedItemName);
		ActivityCompat.startActivity(cxt, intent, options.toBundle());
	}

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		DataBindingUtil.setContentView(this, LAYOUT);
		App.inject(this, getIntent().getStringExtra(EXTRAS_KEYWORD));
	}

	@Inject
	void injected() {
		mPresenter.begin();
	}

	@Override
	protected void onDestroy() {
		mPresenter.end();
		super.onDestroy();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				supportFinishAfterTransition();
				break;
		}
		return super.onOptionsItemSelected(item);
	}
}
