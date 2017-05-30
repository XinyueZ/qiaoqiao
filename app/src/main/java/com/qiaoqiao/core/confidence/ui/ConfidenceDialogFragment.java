package com.qiaoqiao.core.confidence.ui;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
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

	public static ConfidenceDialogFragment newInstance(@NonNull Context cxt) {
		return (ConfidenceDialogFragment) ConfidenceDialogFragment.instantiate(cxt, ConfidenceDialogFragment.class.getName());
	}

	@NonNull
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		mBinding = FragmentConfidenceDialogBinding.inflate(getActivity().getLayoutInflater());
		mBinding.setSeekBarChangeHandler(this);
		mBinding.setThumbLabel(TextDrawable.builder()
		                                   .beginConfig()
		                                   .width(80)  // width in px
		                                   .height(80) // height in px
		                                   .endConfig()
		                                   .buildRound("0", ResourcesCompat.getColor(getResources(), R.color.colorPrimaryDark, null)));
		mBinding.setThumbLogo(TextDrawable.builder()
		                                  .beginConfig()
		                                  .width(80)  // width in px
		                                  .height(80) // height in px
		                                  .endConfig()
		                                  .buildRound("0", ResourcesCompat.getColor(getResources(), R.color.colorPrimaryDark, null)));
		mBinding.setThumbImage(TextDrawable.builder()
		                                   .beginConfig()
		                                   .width(80)  // width in px
		                                   .height(80) // height in px
		                                   .endConfig()
		                                   .buildRound("0", ResourcesCompat.getColor(getResources(), R.color.colorPrimaryDark, null)));

		builder.setView(mBinding.getRoot())
		       .setTitle(R.string.confidence_title)
		       .setPositiveButton(android.R.string.ok, null);
		// Create the AlertDialog object and return it
		return builder.create();
	}

	public BitmapDrawable writeOnDrawable(Bitmap thumb, String text) {

		Bitmap bm = thumb.copy(Bitmap.Config.ARGB_8888, true);

		Paint paint = new Paint();
		paint.setStyle(Paint.Style.FILL);
		paint.setColor(Color.parseColor("#288ca0"));
		paint.setTextSize(bm.getHeight() / 3);
		Canvas canvas = new Canvas(bm);
		canvas.drawText(text, bm.getWidth() / 4 - bm.getWidth() / 10, bm.getHeight() / 2 + bm.getHeight() / 10, paint);

		return new BitmapDrawable(getResources(), bm);
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
		seekBar.setThumb(TextDrawable.builder()
		                             .beginConfig()
		                             .width(80)  // width in px
		                             .height(80) // height in px
		                             .endConfig()
		                             .buildRound(String.valueOf(progress), ResourcesCompat.getColor(getResources(), R.color.colorPrimaryDark, null)));

	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {

	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {

	}
}

