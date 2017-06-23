package com.qiaoqiao.settings;


import android.content.Context;
import android.support.v7.preference.DialogPreference;
import android.util.AttributeSet;

final class PermissionPreference extends DialogPreference {

	public PermissionPreference(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
	}

	public PermissionPreference(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	public PermissionPreference(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public PermissionPreference(Context context) {
		super(context);
	}
}
