package com.qiaoqiao.core.camera.awareness.ui;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.View;

import com.qiaoqiao.R;
import com.qiaoqiao.core.camera.awareness.map.PlaceWrapper;
import com.qiaoqiao.databinding.FragmentSnapshotPlaceInfoBinding;

public final class SnapshotPlaceInfoFragment extends BottomSheetDialogFragment {
	private static final String EXTRAS_PLACE = SnapshotPlaceInfoFragment.class.getName() + ".EXTRAS.place";
	/**
	 * Main layout for this component.
	 */
	private static final int LAYOUT = R.layout.fragment_snapshot_place_info;
	private BottomSheetBehavior mBehavior;
	private FragmentSnapshotPlaceInfoBinding mBinding;

	public static SnapshotPlaceInfoFragment newInstance(Context context, @NonNull PlaceWrapper placeWrapper) {
		Bundle args = new Bundle(1);
		args.putSerializable(EXTRAS_PLACE, placeWrapper);
		return (SnapshotPlaceInfoFragment) SnapshotPlaceInfoFragment.instantiate(context, SnapshotPlaceInfoFragment.class.getName(), args);
	}


	@NonNull
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		BottomSheetDialog dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
		mBinding = FragmentSnapshotPlaceInfoBinding.bind(View.inflate(getContext(), LAYOUT, null));

		dialog.setContentView(mBinding.getRoot());
		mBehavior = BottomSheetBehavior.from((View) mBinding.getRoot()
		                                                    .getParent());
		mBinding.setPlaceWrapper((PlaceWrapper) getArguments().getSerializable(EXTRAS_PLACE));

		return dialog;
	}


	@Override
	public void onResume() {
		super.onResume();
		if (mBehavior != null && mBinding != null) {
			mBinding.openMapFl.setSelected(true);
		}
	}
}
