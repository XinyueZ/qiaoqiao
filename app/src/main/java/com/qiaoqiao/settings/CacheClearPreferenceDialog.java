package com.qiaoqiao.settings;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.preference.PreferenceDialogFragmentCompat;
import android.widget.Toast;

import com.qiaoqiao.R;
import com.qiaoqiao.app.App;


public class CacheClearPreferenceDialog extends PreferenceDialogFragmentCompat {

	private static final String EXTRAS_KEY = "key";

	public static CacheClearPreferenceDialog newInstance(@NonNull Context cxt) {
		Bundle args = new Bundle(1);
		args.putString(EXTRAS_KEY, cxt.getString(R.string.preference_key_datastore_clear_cache));
		return (CacheClearPreferenceDialog) Fragment.instantiate(cxt, CacheClearPreferenceDialog.class.getName(), args);
	}

	@Override
	public void onDialogClosed(boolean b) {
		if (b) {
			App.Companion.getRealm().executeTransaction(realm -> {
				realm.deleteAll();
				Toast.makeText(getContext(), R.string.preference_feedback_cleared_cache, Toast.LENGTH_LONG).show();
			});
		}
	}
}
