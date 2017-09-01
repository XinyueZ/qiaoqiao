package com.qiaoqiao.settings;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v7.preference.Preference;
import android.support.v7.preference.SwitchPreferenceCompat;
import android.text.TextUtils;

import com.qiaoqiao.R;

import java.util.List;

import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static com.qiaoqiao.settings.PermissionRcKt.RC_CAMERA_PERMISSIONS;
import static com.qiaoqiao.settings.PermissionRcKt.RC_FINE_LOCATION_PERMISSIONS;
import static com.qiaoqiao.settings.PermissionRcKt.RC_READ_EXTERNAL_STORAGE_PERMISSIONS;
import static com.qiaoqiao.settings.PermissionRcKt.RC_WRITE_EXTERNAL_STORAGE_PERMISSIONS;


public final class SettingContentFragment extends AbstractSettingFragment implements EasyPermissions.PermissionCallbacks,
                                                                                     Preference.OnPreferenceClickListener {


	public static Fragment newInstance(@NonNull Context cxt, @NonNull Bundle args) {
		return Fragment.instantiate(cxt, SettingContentFragment.class.getName(), args);
	}

	@Override
	public void onCreatePreferences(Bundle bundle, String s) {
		addPreferencesFromResource();
	}


	@Override
	public void onDisplayPreferenceDialog(Preference preference) {
		if (preference instanceof CacheClearPreference) {
			Bundle extras = preference.getExtras();
			if (extras != null) {
				String value = extras.getString(HEADER);
				if (TextUtils.equals(HEADER_PREFERENCE_TITLE_DATASTORE_CLEAR_CACHE, value)) {
					CacheClearPreferenceDialog cacheClearPreferenceDialog = CacheClearPreferenceDialog.newInstance(getContext());
					cacheClearPreferenceDialog.setTargetFragment(this, 0);
					cacheClearPreferenceDialog.show(getChildFragmentManager(), null);
					return;
				}
			}
		}
		super.onDisplayPreferenceDialog(preference);
	}


	@Override
	public void onResume() {
		super.onResume();
		notifyShown();
		initPermissionPreferences();
	}


	private void addPreferencesFromResource() {
		Bundle args = getArguments();
		if (args != null && !TextUtils.isEmpty(args.getString(HEADER))) {
			String value = args.getString(HEADER);
			if (value == null) {
				return;
			}
			Activity activity = getActivity();
			if (activity != null) {
				switch (value) {
					case HEADER_DATASTORE:
						addPreferencesFromResource(R.xml.preferences_datastore);
						break;
					case HEADER_PERMISSION:
						addPreferencesFromResource(R.xml.preferences_permission);
						break;
				}
			}
		}
	}

	private void initPermissionPreferences() {
		Bundle args = getArguments();
		if (args != null && !TextUtils.isEmpty(args.getString(HEADER))) {
			String value = args.getString(HEADER);
			if (value == null || !TextUtils.equals(value, HEADER_PERMISSION)) {
				return;
			}
		}
		mAppSettingsDialog = new AppSettingsDialog.Builder(this).setPositiveButton(R.string.permission_setting)
		                                                        .build();
		initPermissionPreference(R.string.preference_key_write_external_storage, WRITE_EXTERNAL_STORAGE);
		initPermissionPreference(R.string.preference_key_read_external_storage, READ_EXTERNAL_STORAGE);
		initPermissionPreference(R.string.preference_key_fine_location, ACCESS_FINE_LOCATION);
		initPermissionPreference(R.string.preference_key_camera_usage, CAMERA);
	}


	private void initPermissionPreference(@StringRes int preferenceKeyResId, @NonNull String permission) {
		SwitchPreferenceCompat preference = (SwitchPreferenceCompat) findPreference(getString(preferenceKeyResId));
		preference.setChecked(EasyPermissions.hasPermissions(getContext(), permission));
		preference.setOnPreferenceClickListener(this);
	}

	private void checkPermissionPreference(@StringRes int preferenceKeyResId, boolean check) {
		SwitchPreferenceCompat preference = (SwitchPreferenceCompat) findPreference(getString(preferenceKeyResId));
		preference.setChecked(check);
	}

	private boolean isPermissionPreferenceChecked(@StringRes int preferenceKeyResId) {
		SwitchPreferenceCompat preference = (SwitchPreferenceCompat) findPreference(getString(preferenceKeyResId));
		return preference.isChecked();
	}

	private void notifyShown() {
		Bundle args = getArguments();
		if (args != null) {
			int index = args.getInt(INDEX);
			if (getTargetFragment() == null) {
				throw new NullPointerException("Have your lost to call SettingContentFragment$setTargetFragment?");
			}
			if (!(getTargetFragment() instanceof SettingContentCallback)) {
				throw new IllegalArgumentException("You've set target-fragment however it's not a SettingContentFragment$SettingContentCallback.");
			}
			SettingContentCallback targetFragment = (SettingContentCallback) getTargetFragment();
			targetFragment.onShowSettingContent(index, args.getString(TITLE));
		}
	}

	private AppSettingsDialog mAppSettingsDialog;

	@Override
	public boolean onPreferenceClick(Preference preference) {
		if (TextUtils.equals(getString(R.string.preference_key_write_external_storage), preference.getKey())) {
			if (isPermissionPreferenceChecked(R.string.preference_key_write_external_storage)) {
				EasyPermissions.requestPermissions(this, getString(R.string.permission_relation_to_write_external_storage_text), RC_WRITE_EXTERNAL_STORAGE_PERMISSIONS, WRITE_EXTERNAL_STORAGE);
				return false;
			} else {
				mAppSettingsDialog.show();
				return false;
			}
		} else if (TextUtils.equals(getString(R.string.preference_key_read_external_storage), preference.getKey())) {
			if (isPermissionPreferenceChecked(R.string.preference_key_read_external_storage)) {
				EasyPermissions.requestPermissions(this, getString(R.string.permission_relation_to_read_external_storage_text), RC_READ_EXTERNAL_STORAGE_PERMISSIONS, READ_EXTERNAL_STORAGE);
				return false;
			} else {
				mAppSettingsDialog.show();
				return false;
			}
		} else if (TextUtils.equals(getString(R.string.preference_key_fine_location), preference.getKey())) {
			if (isPermissionPreferenceChecked(R.string.preference_key_fine_location)) {
				EasyPermissions.requestPermissions(this, getString(R.string.permission_relation_to_location_text), RC_FINE_LOCATION_PERMISSIONS, ACCESS_FINE_LOCATION);
				return false;
			} else {
				mAppSettingsDialog.show();
				return false;
			}
		} else if (TextUtils.equals(getString(R.string.preference_key_camera_usage), preference.getKey())) {
			if (isPermissionPreferenceChecked(R.string.preference_key_camera_usage)) {
				if (!EasyPermissions.hasPermissions(getContext(), CAMERA, RECORD_AUDIO)) {
					EasyPermissions.requestPermissions(this, getString(R.string.permission_relation_to_camera_text), RC_CAMERA_PERMISSIONS, CAMERA, RECORD_AUDIO);
				}
				return false;
			} else {
				mAppSettingsDialog.show();
				return false;
			}
		}
		return false;
	}

	@Override
	public void onPermissionsGranted(int i, List<String> list) {
		if (list.contains(READ_EXTERNAL_STORAGE)) {
			checkPermissionPreference(R.string.preference_key_read_external_storage, true);
		}
		if (list.contains(WRITE_EXTERNAL_STORAGE)) {
			checkPermissionPreference(R.string.preference_key_write_external_storage, true);
		}
		if (list.contains(ACCESS_FINE_LOCATION)) {
			checkPermissionPreference(R.string.preference_key_fine_location, true);
		}
		if (list.contains(CAMERA) && list.contains(RECORD_AUDIO)) {
			checkPermissionPreference(R.string.preference_key_camera_usage, true);
		}
	}


	@Override
	public void onPermissionsDenied(int i, List<String> perms) {
		if (EasyPermissions.permissionPermanentlyDenied(this, READ_EXTERNAL_STORAGE)) {
			checkPermissionPreference(R.string.preference_key_read_external_storage, false);
		}
		if (EasyPermissions.permissionPermanentlyDenied(this, WRITE_EXTERNAL_STORAGE)) {
			checkPermissionPreference(R.string.preference_key_write_external_storage, false);
		}
		if (EasyPermissions.permissionPermanentlyDenied(this, ACCESS_FINE_LOCATION)) {
			checkPermissionPreference(R.string.preference_key_fine_location, false);
		}
		if (EasyPermissions.permissionPermanentlyDenied(this, CAMERA) || EasyPermissions.permissionPermanentlyDenied(this, RECORD_AUDIO)) {
			checkPermissionPreference(R.string.preference_key_camera_usage, false);
		}
	}


	interface SettingContentCallback {
		void onShowSettingContent(int index, String title);
	}
}
