package com.qiaoqiao.core.confidence.ui;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;

import com.qiaoqiao.R;
import com.qiaoqiao.core.confidence.ConfidenceContract;
import com.qiaoqiao.core.confidence.ConfidencePresenter;
import com.qiaoqiao.databinding.FragmentConfidenceDialogBinding;


public final class ConfidenceDialogFragment extends AppCompatDialogFragment implements ConfidenceContract.View {
	private ConfidenceContract.Presenter mPresenter;
	private FragmentConfidenceDialogBinding mBinding;

	public static ConfidenceDialogFragment newInstance(@NonNull Context cxt) {
		return (ConfidenceDialogFragment) ConfidenceDialogFragment.instantiate(cxt, ConfidenceDialogFragment.class.getName());
	}

	@NonNull
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setView(FragmentConfidenceDialogBinding.inflate(getActivity().getLayoutInflater())
		                                               .getRoot())
		       .setTitle(R.string.confidence_title)
		       .setPositiveButton(android.R.string.ok, null);
		// Create the AlertDialog object and return it
		return builder.create();
	}

	@Override
	public void setPresenter(@NonNull ConfidencePresenter presenter) {
		mPresenter = presenter;
	}

	@Override
	public FragmentConfidenceDialogBinding getBinding() {
		return null;
	}
}
