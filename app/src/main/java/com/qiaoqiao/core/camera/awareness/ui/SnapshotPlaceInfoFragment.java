package com.qiaoqiao.core.camera.awareness.ui;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.BottomSheetDialogFragment;
import android.text.TextUtils;
import android.view.View;

import com.bumptech.glide.Glide;
import com.qiaoqiao.R;
import com.qiaoqiao.core.camera.awareness.map.PlaceWrapper;
import com.qiaoqiao.core.location.ui.MapActivity;
import com.qiaoqiao.customtabs.CustomTabUtils;
import com.qiaoqiao.databinding.FragmentSnapshotPlaceInfoBinding;

public final class SnapshotPlaceInfoFragment extends BottomSheetDialogFragment implements View.OnClickListener {
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
		mBinding.setClickHandler(this);
		return dialog;
	}


	@Override
	public void onResume() {
		super.onResume();
		if (mBehavior != null && mBinding != null) {
			mBinding.openMapFl.setActivated(true);
		}
	}

	@Override
	public void onClick(View v) {
		final PlaceWrapper placeWrapper = (PlaceWrapper) getArguments().getSerializable(EXTRAS_PLACE);
		if (placeWrapper == null) {
			return;
		}
		dismiss();
		switch (v.getId()) {
			case R.id.web_tv:
				CustomTabUtils.openWeb(SnapshotPlaceInfoFragment.this,
				                       placeWrapper.getTitle(),
				                       placeWrapper.getPlace()
				                                   .getWebsiteUri());
				break;
			case R.id.tel_tv:
				callPhoneNumber(getContext(),
				                placeWrapper.getPlace()
				                            .getPhoneNumber()
				                            .toString());
				break;
			default:
				MapActivity.showInstance(getActivity(), placeWrapper.getPosition(), mBinding.openMapBtn);
				break;
		}

	}


	private static String getSanitizedPhoneNumber(String phoneNumber) {
		return phoneNumber.replaceAll("[^0-9+]+", "");
	}


	private static Intent createCallPhoneNumberIntent(String phoneNumber) {
		Intent ret = null;
		if (phoneNumber != null) {
			phoneNumber = getSanitizedPhoneNumber(phoneNumber);
			if (!TextUtils.isEmpty(phoneNumber)) {
				ret = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNumber));
			}
		}
		return ret;
	}

	private static boolean callPhoneNumber(Context ctx, String phoneNumber) {
		boolean ret;
		Intent intent = createCallPhoneNumberIntent(phoneNumber);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		try {
			ctx.startActivity(intent);
			ret = true;
		} catch (ActivityNotFoundException e) {
			ret = false;
		}
		return ret;
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		Glide.clear(mBinding.placeIv);
	}
}
