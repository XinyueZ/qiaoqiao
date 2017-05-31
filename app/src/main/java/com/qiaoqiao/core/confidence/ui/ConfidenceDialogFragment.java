package com.qiaoqiao.core.confidence.ui;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Px;
import android.support.v4.content.SharedPreferencesCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.support.v7.preference.PreferenceManager;
import android.widget.SeekBar;

import com.amulyakhare.textdrawable.TextDrawable;
import com.qiaoqiao.R;
import com.qiaoqiao.core.confidence.ConfidenceContract;
import com.qiaoqiao.core.confidence.ConfidencePresenter;
import com.qiaoqiao.databinding.FragmentConfidenceDialogBinding;

import static com.qiaoqiao.app.PrefsKeys.KEY_CONFIDENCE_IMAGE;
import static com.qiaoqiao.app.PrefsKeys.KEY_CONFIDENCE_LABEL;
import static com.qiaoqiao.app.PrefsKeys.KEY_CONFIDENCE_LOGO;


public final class ConfidenceDialogFragment extends AppCompatDialogFragment implements ConfidenceContract.View,
                                                                                       SeekBar.OnSeekBarChangeListener,
                                                                                       DialogInterface.OnClickListener {
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
		final float cfLabel = readPrefs(KEY_CONFIDENCE_LABEL);
		final float cfLogo = readPrefs(KEY_CONFIDENCE_LOGO);
		final float cfImage = readPrefs(KEY_CONFIDENCE_IMAGE);
		mBinding.setThumbLabel(createThumbDrawable(cfLabel));
		mBinding.setThumbLogo(createThumbDrawable(cfLogo));
		mBinding.setThumbImage(createThumbDrawable(cfImage));
		mBinding.setCfLabel((int) ((cfLabel) * 100));
		mBinding.setCfLogo((int) ((cfLogo) * 100));
		mBinding.setCfImage((int) ((cfImage) * 100));
		builder.setView(mBinding.getRoot())
		       .setTitle(R.string.confidence_title)
		       .setPositiveButton(android.R.string.ok, this)
		       .setNegativeButton(android.R.string.cancel, null);
		// Create the AlertDialog object and return it
		return builder.create();
	}

	@Override
	public void onClick(DialogInterface dialog, int which) {
		writePrefs();
	}

	private void writePrefs() {
		writePrefs(KEY_CONFIDENCE_LABEL, mBinding.confidenceLabelValue.getProgress());
		writePrefs(KEY_CONFIDENCE_LOGO, mBinding.confidenceLogoValue.getProgress());
		writePrefs(KEY_CONFIDENCE_IMAGE, mBinding.confidenceImageValue.getProgress());
	}

	private void writePrefs(@NonNull String key, int value) {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
		final SharedPreferences.Editor edit = prefs.edit();
		edit.putFloat(key, value / 100.f);
		SharedPreferencesCompat.EditorCompat.getInstance()
		                                    .apply(edit);
	}

	private float readPrefs(@NonNull String key) {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
		return prefs.getFloat(key, 0.f);
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

