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

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.OnStreetViewPanoramaReadyCallback;
import com.google.android.gms.maps.StreetViewPanorama;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.StreetViewPanoramaLocation;
import com.qiaoqiao.R;
import com.qiaoqiao.databinding.ActivityMapBinding;
import com.qiaoqiao.utils.SystemUiHelper;

import static android.os.Bundle.EMPTY;


public final class MapActivity extends AppCompatActivity implements OnMapReadyCallback,
                                                                    OnStreetViewPanoramaReadyCallback,
                                                                    StreetViewPanorama.OnStreetViewPanoramaChangeListener,
                                                                    GoogleMap.OnMapClickListener {
	private static final int LAYOUT = R.layout.activity_map;
	private static final String EXTRAS_LATLNG = MapActivity.class.getName() + ".EXTRAS.latlng";
	private @Nullable ActivityMapBinding mBinding;
	private @Nullable SupportMapFragment mSupportMapFragment;

	public static void showInstance(@NonNull Activity cxt, @NonNull LatLng latLng) {
		Intent intent = new Intent(cxt, MapActivity.class);
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
		mSupportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
		mSupportMapFragment.getMapAsync(this);
		mBinding.streetview.getStreetViewPanoramaAsync(this);
		mSupportMapFragment.onCreate(savedInstanceState);
		mSupportMapFragment.onStart();
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (mSupportMapFragment != null) {
			mSupportMapFragment.onPause();
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (mSupportMapFragment != null) {
			mSupportMapFragment.onResume();
		}
	}

	@Override
	protected void onStart() {
		super.onStart();
		if (mSupportMapFragment != null) {
			mSupportMapFragment.onStart();
		}
	}

	@Override
	protected void onStop() {
		super.onStop();
		if (mSupportMapFragment != null) {
			mSupportMapFragment.onStop();
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (mSupportMapFragment != null) {
			mSupportMapFragment.onDestroy();
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


	private @Nullable GoogleMap mGoogleMap;

	@Override
	public void onMapReady(GoogleMap googleMap) {
		mGoogleMap = googleMap;
		mGoogleMap.setOnMapClickListener(this);
		googleMap.getUiSettings()
		         .setMapToolbarEnabled(false);
		if (mGoogleMap != null) {
			moveToLocation(getIntent(), googleMap);
		}
	}

	private static void moveToLocation(@NonNull Intent intent, @NonNull GoogleMap googleMap) {
		LatLng latLng = intent.getParcelableExtra(EXTRAS_LATLNG);
		googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));
		googleMap.addMarker(new MarkerOptions().position(latLng));
	}

	public void onClickDirectionButton(View view) {
		final Intent intent = getIntent();
		LatLng latLng = intent.getParcelableExtra(EXTRAS_LATLNG);
		RouteCalcClientPicker.show(this, latLng);
	}

	public void onClickLocationButton(View view) {
		if (mGoogleMap != null) {
			moveToLocation(getIntent(), mGoogleMap);
		}
	}

	public void onClickStreetViewButton(View view) {
		final Intent intent = getIntent();
		LatLng latLng = intent.getParcelableExtra(EXTRAS_LATLNG);
		StreetViewActivity.showInstance(this, latLng);
	}

	@Override
	public void onStreetViewPanoramaReady(StreetViewPanorama streetViewPanorama) {
		streetViewPanorama.setOnStreetViewPanoramaChangeListener(this);
		final Intent intent = getIntent();
		LatLng latLng = intent.getParcelableExtra(EXTRAS_LATLNG);
		streetViewPanorama.setPosition(latLng);
	}

	@Override
	public void onStreetViewPanoramaChange(StreetViewPanoramaLocation streetViewPanoramaLocation) {
		if (streetViewPanoramaLocation != null && streetViewPanoramaLocation.links != null) {
			return;
		}
		if (mBinding != null) {
			mBinding.streetviewFab.setVisibility(View.GONE);
		}
	}

	@Override
	public void onMapClick(LatLng latLng) {

	}
}
