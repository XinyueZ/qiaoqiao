package com.qiaoqiao.core.confidence.ui;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Px;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.widget.SeekBar;

import com.amulyakhare.textdrawable.TextDrawable;
import com.qiaoqiao.R;
import com.qiaoqiao.core.confidence.ConfidenceContract;
import com.qiaoqiao.core.confidence.ConfidencePresenter;
import com.qiaoqiao.databinding.FragmentConfidenceDialogBinding;


public final class ConfidenceDialogFragment extends AppCompatDialogFragment implements ConfidenceContract.View,
                                                                                       SeekBar.OnSeekBarChangeListener {
	private ConfidenceContract.Presenter mPresenter;
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
		mBinding.setSeekBarChangeHandler(this);
		mBinding.setThumbLabel(createThumbDrawable(0));
		mBinding.setThumbLogo(createThumbDrawable(0));
		mBinding.setThumbImage(createThumbDrawable(0));
		builder.setView(mBinding.getRoot())
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
		return mBinding;
	}

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//		TextDrawable textDrawable = (TextDrawable) seekBar.getThumb();
//		textDrawable.
		seekBar.setThumb(createThumbDrawable(progress / 100.f));

	}

	private TextDrawable createThumbDrawable(float progress) {
		final Resources resources = getResources();
		return TextDrawable.builder()
		                   .beginConfig()
		                   .width(W)
		                   .height(H)
		                   .textColor(ResourcesCompat.getColor(resources, R.color.colorYellow, null))
		                   .endConfig()
		                   .buildRound(String.valueOf(progress), ResourcesCompat.getColor(resources, R.color.colorPrimaryDark, null));
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {

	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {

	}
}

