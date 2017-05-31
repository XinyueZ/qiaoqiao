package com.qiaoqiao.core.splash;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.SharedPreferencesCompat;
import android.support.v7.content.res.AppCompatResources;
import android.support.v7.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.qiaoqiao.R;
import com.qiaoqiao.app.PrefsKeys;
import com.qiaoqiao.databinding.FragmentGplusBinding;
import com.qiaoqiao.utils.DeviceUtils;

import static com.qiaoqiao.app.PrefsKeys.DEFAULT_GOOGLE_DISPLAY_NAME;
import static com.qiaoqiao.app.PrefsKeys.DEFAULT_GOOGLE_ID;
import static com.qiaoqiao.app.PrefsKeys.DEFAULT_GOOGLE_PHOTO_URL;
import static com.qiaoqiao.app.PrefsKeys.KEY_GOOGLE_DISPLAY_NAME;
import static com.qiaoqiao.app.PrefsKeys.KEY_GOOGLE_ID;
import static com.qiaoqiao.app.PrefsKeys.KEY_GOOGLE_PHOTO_URL;

/**
 * The fragment that controls user information of g+, signOut etc.
 *
 * @author Xinyue Zhao
 */
public final class GPlusFragment extends Fragment implements OnClickListener {

	private FragmentGplusBinding mBinding;

	/**
	 * New an instance of {@link GPlusFragment}.
	 *
	 * @param context {@link Context}.
	 * @return An instance of {@link GPlusFragment}.
	 */
	public static Fragment newInstance(Context context) {
		return GPlusFragment.instantiate(context, GPlusFragment.class.getName());
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mBinding = FragmentGplusBinding.inflate(inflater, container, false);
		mBinding.setClickHandler(this);
		mBinding.peoplePhotoIv.setImageDrawable(AppCompatResources.getDrawable(getContext(), R.drawable.ic_people));
		return mBinding.getRoot();
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		mBinding.getRoot()
		        .getLayoutParams().height = (int) Math.ceil(DeviceUtils.getScreenSize(getContext()).Height * (1 - 0.618f));


	}

	private void signIn() {
		ConnectGoogleActivity.showInstance(getActivity(), false);
	}

	private void signOut() {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
		final SharedPreferences.Editor edit = prefs.edit();
		edit.putString(KEY_GOOGLE_ID, DEFAULT_GOOGLE_ID);
		edit.putString(KEY_GOOGLE_PHOTO_URL, DEFAULT_GOOGLE_PHOTO_URL);
		edit.putString(KEY_GOOGLE_DISPLAY_NAME, DEFAULT_GOOGLE_DISPLAY_NAME);
		SharedPreferencesCompat.EditorCompat.getInstance()
		                                    .apply(edit);
		ConnectGoogleActivity.showInstance(getActivity(), true);
	}


	@Override
	public void onResume() {
		showUIStatus();
		super.onResume();
	}

	private void showUIStatus() {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
		if (TextUtils.isEmpty(prefs.getString(PrefsKeys.KEY_GOOGLE_ID, DEFAULT_GOOGLE_ID))) {
			mBinding.btn.setText(R.string.login_google);
			mBinding.getRoot()
			        .setBackgroundResource(R.color.colorBlueGrey);
			mBinding.peopleNameTv.setText("");
			mBinding.peoplePhotoIv.setImageDrawable(AppCompatResources.getDrawable(getContext(), R.drawable.ic_people));

		} else {
			mBinding.btn.setText(R.string.logout_google);
			String name = prefs.getString(PrefsKeys.KEY_GOOGLE_DISPLAY_NAME, null);
			String thumbnailUrl = prefs.getString(KEY_GOOGLE_PHOTO_URL, null);
			mBinding.getRoot()
			        .setBackgroundResource(R.color.colorPrimary);
			if (!TextUtils.isEmpty(thumbnailUrl)) {
				Glide.with(this)
				     .load(thumbnailUrl)
				     .error(AppCompatResources.getDrawable(getContext(), R.drawable.ic_people))
				     .placeholder(AppCompatResources.getDrawable(getContext(), R.drawable.ic_people))
				     .into(mBinding.peoplePhotoIv);
			}
			if (!TextUtils.isEmpty(name)) {
				mBinding.peopleNameTv.setText(name);
			}
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.btn:
				SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
				if (TextUtils.isEmpty(prefs.getString(PrefsKeys.KEY_GOOGLE_ID, DEFAULT_GOOGLE_ID))) {
					signIn();
				} else {
					signOut();
				}
				break;
		}
	}
}
