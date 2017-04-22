package com.qiaoqiao.ds.web.ui;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.qiaoqiao.R;
import com.qiaoqiao.bus.WebLinkInputEvent;
import com.qiaoqiao.databinding.FragmentInputWebLinkBinding;

import de.greenrobot.event.EventBus;

public final class FromInputWebLinkFragment extends Fragment implements View.OnClickListener {
	/**
	 * Main layout for this component.
	 */
	private static final int LAYOUT = R.layout.fragment_input_web_link;
	private FragmentInputWebLinkBinding mBinding;

	public static FromInputWebLinkFragment newInstance(@NonNull Context cxt) {
		return (FromInputWebLinkFragment) FromInputWebLinkFragment.instantiate(cxt, FromInputWebLinkFragment.class.getName());
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		mBinding = DataBindingUtil.inflate(inflater, LAYOUT, container, false);
		mBinding.uriTv.setText("https://upload.wikimedia.org/wikipedia/commons/d/d4/Hradschin_Prag.jpg");
		mBinding.takeUriBtn.setOnClickListener(this);
		return mBinding.getRoot();
	}


	@Override
	public void onClick(View v) {
		EventBus.getDefault()
		        .post(new WebLinkInputEvent(Uri.parse(mBinding.uriTv.getText().toString())));
	}
}
