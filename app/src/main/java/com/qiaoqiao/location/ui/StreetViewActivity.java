package com.qiaoqiao.location.ui;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.google.android.gms.maps.OnStreetViewPanoramaReadyCallback;
import com.google.android.gms.maps.StreetViewPanorama;
import com.google.android.gms.maps.SupportStreetViewPanoramaFragment;
import com.google.android.gms.maps.model.LatLng;
import com.qiaoqiao.R;
import com.qiaoqiao.databinding.ActivityStreetviewBinding;
import com.qiaoqiao.utils.SystemUiHelper;

import static android.os.Bundle.EMPTY;


public final class StreetViewActivity extends AppCompatActivity implements OnStreetViewPanoramaReadyCallback {
	private static final int LAYOUT = R.layout.activity_streetview;
	private static final String EXTRAS_LATLNG = StreetViewActivity.class.getName() + ".EXTRAS.latlng";
	private @Nullable ActivityStreetviewBinding mBinding;
	private @Nullable SupportStreetViewPanoramaFragment mStreetViewPanoramaFragment;

	public static void showInstance(@NonNull Activity cxt, @NonNull LatLng latLng) {
		Intent intent = new Intent(cxt, StreetViewActivity.class);
		intent.putExtra(EXTRAS_LATLNG, latLng);
		intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
		ActivityCompat.startActivity(cxt, intent, EMPTY);
	}

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mBinding = DataBindingUtil.setContentView(this, LAYOUT);
		mBinding.setActivity(this);
		SystemUiHelper uiHelper = new SystemUiHelper(this, SystemUiHelper.LEVEL_IMMERSIVE, 0);
		uiHelper.hide();
		mBinding.setUiHelper(uiHelper);
		mStreetViewPanoramaFragment = (SupportStreetViewPanoramaFragment) getSupportFragmentManager().findFragmentById(R.id.panorama);
		mStreetViewPanoramaFragment.onCreate(savedInstanceState);
		mStreetViewPanoramaFragment.getStreetViewPanoramaAsync(this);
	}


	@Override
	protected void onPause() {
		super.onPause();
		if (mStreetViewPanoramaFragment != null) {
			mStreetViewPanoramaFragment.onPause();
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (mStreetViewPanoramaFragment != null) {
			mStreetViewPanoramaFragment.onResume();
		}
	}

	@Override
	protected void onStart() {
		super.onStart();
		if (mStreetViewPanoramaFragment != null) {
			mStreetViewPanoramaFragment.onStart();
		}
	}

	@Override
	protected void onStop() {
		super.onStop();
		if (mStreetViewPanoramaFragment != null) {
			mStreetViewPanoramaFragment.onStop();
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (mStreetViewPanoramaFragment != null) {
			mStreetViewPanoramaFragment.onDestroy();
		}
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		if (mBinding != null) {
			mBinding.getUiHelper()
			        .hide();
		}
		super.onWindowFocusChanged(hasFocus);
	}

	private @Nullable StreetViewPanorama mStreetViewPanorama;

	@Override
	public void onStreetViewPanoramaReady(StreetViewPanorama streetViewPanorama) {
		mStreetViewPanorama = streetViewPanorama;
		if (mStreetViewPanorama != null) {
			moveToLocation(getIntent(), mStreetViewPanorama);
		}
	}

	private static void moveToLocation(@NonNull Intent intent, @NonNull StreetViewPanorama streetViewPanorama) {
		LatLng latLng = intent.getParcelableExtra(EXTRAS_LATLNG);
		streetViewPanorama.setPosition(latLng);
	}

	public void onClickDirectionButton(View view) {
		final Intent intent = getIntent();
		LatLng latLng = intent.getParcelableExtra(EXTRAS_LATLNG);
		RouteCalcClientPicker.show(this, latLng);
	}


	public void onClickLocationButton(View view) {
		if (mStreetViewPanorama != null) {
			moveToLocation(getIntent(), mStreetViewPanorama);
		}
	}

	public void onClickMapButton(View view) {
		final Intent intent = getIntent();
		LatLng latLng = intent.getParcelableExtra(EXTRAS_LATLNG);
		MapActivity.showInstance(this, latLng);
	}


}