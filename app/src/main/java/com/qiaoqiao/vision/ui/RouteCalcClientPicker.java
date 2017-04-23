package com.qiaoqiao.vision.ui;

import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatDialogFragment;
import android.widget.Toast;

import com.google.api.services.vision.v1.model.LatLng;
import com.qiaoqiao.R;

import static android.text.TextUtils.isEmpty;


public final class RouteCalcClientPicker {

	private static final String STORE_NATIVE_TO_MAP = "market://search?q=map&c=apps";
	private static final String STORE_WEB_TO_MAP = "https://play.google.com/store/search?q=map&c=apps";
	private static final String STATIC_MAP = "http://maps.google.com/maps?daddr=";

	private RouteCalcClientPicker() {
		//Kill constructor.
	}

	public static void show(@NonNull FragmentActivity activity, @NonNull LatLng latLng) {
		try {
			startRouteCalc(activity, latLng);
		} catch (ActivityNotFoundException e) {
			showDialogFragment(activity.getSupportFragmentManager(), MapAppNotAvailableDialFragment.newInstance(activity), MapAppNotAvailableDialFragment.TAG);
		}
	}

	private static void startRouteCalc(@NonNull Activity activity, @NonNull LatLng latLng) {
		String strLatitude = String.valueOf(latLng.getLatitude());
		String strLongitude = String.valueOf(latLng.getLongitude());

		String url = STATIC_MAP + strLatitude + "," + strLongitude;
		Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
		ActivityCompat.startActivity(activity, intent, Bundle.EMPTY);
	}

	private static void showDialogFragment(@NonNull FragmentManager mgr, @NonNull AppCompatDialogFragment dlgFrg, String tagName) {
		FragmentTransaction fragmentTransaction = mgr.beginTransaction();
		// Ensure that there's only one dialog to the user.
		Fragment prev = mgr.findFragmentByTag("dlg");
		if (prev != null) {
			fragmentTransaction.remove(prev);
		}

		dlgFrg.show(fragmentTransaction,
		            isEmpty(tagName) ?
		            "dlg" :
		            tagName);
	}

	public static final class MapAppNotAvailableDialFragment extends AppCompatDialogFragment {
		public static final String TAG = MapAppNotAvailableDialFragment.class.getName();

		public static MapAppNotAvailableDialFragment newInstance(Context cxt) {
			return (MapAppNotAvailableDialFragment) MapAppNotAvailableDialFragment.instantiate(cxt, MapAppNotAvailableDialFragment.class.getName());
		}

		@NonNull
		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			// Use the Builder class for convenient dialog construction
			android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(getActivity());
			builder.setTitle(R.string.app_name)
			       .setMessage(R.string.no_map_apps)
			       .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
				       public void onClick(DialogInterface dialog, int id) {
					       try {
						       startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(STORE_NATIVE_TO_MAP)));
					       } catch (ActivityNotFoundException exx) {
						       try {
							       startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(STORE_WEB_TO_MAP)));
						       } catch (ActivityNotFoundException exxx) {
							       Toast.makeText(getContext(), R.string.no_map_app_title, Toast.LENGTH_LONG)
							            .show();
						       }
					       }
				       }
			       })
			       .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
				       public void onClick(DialogInterface dialog, int id) {
					       // User cancelled the dialog
				       }
			       });
			// Create the AlertDialog object and return it
			return builder.create();
		}
	}
}
