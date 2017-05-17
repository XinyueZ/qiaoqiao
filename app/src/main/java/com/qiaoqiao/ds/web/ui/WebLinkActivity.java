package com.qiaoqiao.ds.web.ui;


import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.qiaoqiao.R;

public final class WebLinkActivity extends AppCompatActivity {
	public static final int REQ = 0x80;
	private static final int LAYOUT = R.layout.activity_web_link;


	public static void showInstance(@NonNull Activity cxt, @NonNull View transitionView) {
		Intent intent = new Intent(cxt, WebLinkActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
		ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(cxt, transitionView, cxt.getString(R.string.transition_share_item_name));
		ActivityCompat.startActivityForResult(cxt, intent, REQ, options.toBundle());
	}

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		DataBindingUtil.setContentView(this, LAYOUT);
	}
}
