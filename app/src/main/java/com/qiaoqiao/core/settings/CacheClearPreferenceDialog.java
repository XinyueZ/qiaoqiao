package com.qiaoqiao.core.settings;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.preference.PreferenceDialogFragmentCompat;
import android.widget.Toast;

import com.qiaoqiao.R;


public class CacheClearPreferenceDialog extends PreferenceDialogFragmentCompat {

	private static final String KEY = "key";

	public static CacheClearPreferenceDialog newInstance(@NonNull Context cxt) {
		Bundle args = new Bundle(1);
		args.putString(KEY, cxt.getString(R.string.preference_key_datastore_clear_cache));
		return (CacheClearPreferenceDialog) Fragment.instantiate(cxt, CacheClearPreferenceDialog.class.getName(), args);
	}

	@Override
	public void onDialogClosed(boolean b) {
		if (b) {
			Toast.makeText(getContext(), "Cache has been cleared.", Toast.LENGTH_LONG)
			     .show();
		}
	}
}
