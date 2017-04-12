package com.qiaoqiao.ds.ui;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.qiaoqiao.R;
import com.qiaoqiao.databinding.FragmentInputWebLinkBinding;

public final class FromInputWebLinkFragment extends Fragment {
	/**
	 * Main layout for this component.
	 */
	private static final int LAYOUT = R.layout.fragment_input_web_link;

	public static FromInputWebLinkFragment newInstance(@NonNull Context cxt) {
		return (FromInputWebLinkFragment) FromInputWebLinkFragment.instantiate(cxt, FromInputWebLinkFragment.class.getName());
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		FragmentInputWebLinkBinding binding = DataBindingUtil.inflate(inflater, LAYOUT, container, false);
		return binding.getRoot();
	}
}
