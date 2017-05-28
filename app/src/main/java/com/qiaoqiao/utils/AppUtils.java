package com.qiaoqiao.utils;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.qiaoqiao.R;

public class AppUtils {

	private AppUtils() {
	}

	public static void goToPlayServiceDownload(@NonNull Context cxt) {
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setData(Uri.parse(cxt.getString(R.string.play_service_url)));
		try {
			cxt.startActivity(intent);
		} catch (ActivityNotFoundException e0) {
			intent.setData(Uri.parse(cxt.getString(R.string.play_service_web)));
			try {
				cxt.startActivity(intent);
			} catch (Exception e1) {
			}
		}
	}
}
