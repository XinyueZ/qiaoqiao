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
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.qiaoqiao.R;
import com.qiaoqiao.databinding.ActivityMapBinding;

import static android.os.Bundle.EMPTY;


public final class MapActivity extends AppCompatActivity implements OnMapReadyCallback {
	private static final int LAYOUT = R.layout.activity_map;
	private static final String EXTRAS_LATLNG = MapActivity.class.getName() + ".EXTRAS.latlng";

	public static void showInstance(@NonNull Activity cxt, @NonNull LatLng latLng) {
		Intent intent = new Intent(cxt, MapActivity.class);
		intent.putExtra(EXTRAS_LATLNG, latLng);
		intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
		ActivityCompat.startActivity(cxt, intent, EMPTY);
	}

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActivityMapBinding binding = DataBindingUtil.setContentView(this, LAYOUT);
		binding.setActivity(this);
		SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
		supportMapFragment.getMapAsync(this);
	}

	private @Nullable GoogleMap mGoogleMap;

	@Override
	public void onMapReady(GoogleMap googleMap) {
		mGoogleMap = googleMap;
		googleMap.getUiSettings()
		         .setMapToolbarEnabled(false);
		if (mGoogleMap != null) {
			moveToLocation(getIntent(), googleMap);
		}
	}

	private static void moveToLocation(@NonNull Intent intent, @NonNull GoogleMap googleMap) {
		LatLng latLng = intent.getParcelableExtra(EXTRAS_LATLNG);
		googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));
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
}
