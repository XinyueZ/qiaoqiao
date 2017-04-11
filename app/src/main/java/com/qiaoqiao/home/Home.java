package com.qiaoqiao.home;


import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Toast;

import com.qiaoqiao.views.MainControl;

import javax.inject.Inject;

public final class Home {
	private final MainControl mMainControl;

	@Inject
	public Home(@NonNull MainControl mainControl) {
		mMainControl = mainControl;
		mMainControl.setOnFromLocalClickedListener(new MainControl.OnFromLocalClickedListener() {
			@Override
			public void onClick(View v) {
				Toast.makeText(v.getContext(), "setOnFromLocalClickedListener", Toast.LENGTH_SHORT)
				     .show();
			}
		});
		mMainControl.setOnCaptureClickedListener(new MainControl.OnCaptureClickedListener() {
			@Override
			public void onClick(View v) {

				Toast.makeText(v.getContext(), "setOnCaptureClickedListener", Toast.LENGTH_SHORT)
				     .show();
			}
		});
		mMainControl.setOnFromWebClickedListener(new MainControl.OnFromWebClickedListener() {
			@Override
			public void onClick(View v) {

				Toast.makeText(v.getContext(), "setOnFromWebClickedListener", Toast.LENGTH_SHORT)
				     .show();
			}
		});
	}
}
