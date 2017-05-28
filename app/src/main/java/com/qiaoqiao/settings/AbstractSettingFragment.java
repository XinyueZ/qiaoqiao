package com.qiaoqiao.settings;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.qiaoqiao.R;


abstract class AbstractSettingFragment extends PreferenceFragmentCompat {
	static final String HEADER = "header";
	static final String TITLE = "title";
	static final String INDEX = "index";
	static final String HEADER_CAMERA = "preferences_camera";
	static final String HEADER_DATASTORE = "preferences_datastore";
	static final String HEADER_PREFERENCE_TITLE_DATASTORE_CLEAR_CACHE = "preference_title_datastore_clear_cache";


	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		view.setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.colorWhite, null));

		RecyclerView listView = getListView();
		listView.setPadding(0, 0, 0, 0);
	}
}
