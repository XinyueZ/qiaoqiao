package com.qiaoqiao.core.confidence.ui;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.Px;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;

import com.qiaoqiao.R;
import com.qiaoqiao.core.confidence.ConfidenceContract;
import com.qiaoqiao.core.confidence.ConfidencePresenter;
import com.qiaoqiao.databinding.FragmentConfidenceDialogBinding;

import static com.qiaoqiao.app.PrefsKeys.KEY_CONFIDENCE_IMAGE;
import static com.qiaoqiao.app.PrefsKeys.KEY_CONFIDENCE_LABEL;
import static com.qiaoqiao.app.PrefsKeys.KEY_CONFIDENCE_LOGO;


public final class ConfidenceDialogFragment extends AppCompatDialogFragment implements ConfidenceContract.View,
                                                                                       DialogInterface.OnClickListener {
	private @Nullable ConfidenceContract.Presenter mPresenter;
	private FragmentConfidenceDialogBinding mBinding;
	private @Px static final int W = 95;
	private @Px static final int H = 95;

	public static ConfidenceDialogFragment newInstance(@NonNull Context cxt) {
		return (ConfidenceDialogFragment) ConfidenceDialogFragment.instantiate(cxt, ConfidenceDialogFragment.class.getName());
	}

	@NonNull
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		mBinding = FragmentConfidenceDialogBinding.inflate(getActivity().getLayoutInflater());
		builder.setView(mBinding.getRoot())
		       .setTitle(R.string.confidence_title)
		       .setPositiveButton(android.R.string.ok, this)
		       .setNegativeButton(android.R.string.cancel, null);
		// Create the AlertDialog object and return it
		return builder.create();
	}


	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		if (mPresenter != null) {
			mPresenter.loadAllConfidences(getContext());
		}
	}

	@Override
	public void onClick(DialogInterface dialog, int which) {
		saveAllConfidences();
	}


	@Override
	public void showLabelConfidence(@NonNull Confidence confidence) {
		mBinding.setConfidenceLabel(confidence);
	}

	@Override
	public void showLogoConfidence(@NonNull Confidence confidence) {
		mBinding.setConfidenceLogo(confidence);
	}

	@Override
	public void showImageConfidence(@NonNull Confidence confidence) {
		mBinding.setConfidenceImage(confidence);
	}

	@Override
	public void setPresenter(@NonNull ConfidencePresenter presenter) {
		mPresenter = presenter;
	}

	@Override
	public FragmentConfidenceDialogBinding getBinding() {
		return mBinding;
	}

	private void saveAllConfidences() {
		if (mPresenter == null) {
			return;
		}
		final Context context = getContext();
		mPresenter.save(context, KEY_CONFIDENCE_LABEL, mBinding.confidenceLabelValue.getProgress());
		mPresenter.save(context, KEY_CONFIDENCE_LOGO, mBinding.confidenceLogoValue.getProgress());
		mPresenter.save(context, KEY_CONFIDENCE_IMAGE, mBinding.confidenceImageValue.getProgress());
	}
}

