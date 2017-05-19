package com.qiaoqiao.repository.web.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import com.qiaoqiao.databinding.FragmentWebLinkBinding;

public final class WebLinkFragment extends Fragment implements View.OnClickListener {
	/**
	 * Main layout for this component.
	 */
	private static final int LAYOUT = R.layout.fragment_web_link;
	private FragmentWebLinkBinding mBinding;

	public static WebLinkFragment newInstance(@NonNull Context cxt) {
		return (WebLinkFragment) WebLinkFragment.instantiate(cxt, WebLinkFragment.class.getName());
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		mBinding = DataBindingUtil.inflate(inflater, LAYOUT, container, false);
		mBinding.uriTv.setText(R.string.demo_web);
		mBinding.takeUriBtn.setOnClickListener(this);
		return mBinding.getRoot();
	}


	@Override
	public void onClick(View v) {
		Intent data = new Intent();
		data.setData(Uri.parse(mBinding.uriTv.getText()
		                                     .toString()));
		getActivity().setResult(Activity.RESULT_OK, data);
		getActivity().supportFinishAfterTransition();
	}
}
