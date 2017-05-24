package com.qiaoqiao.core.camera.ui;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.google.android.cameraview.CameraView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.api.services.vision.v1.model.BatchAnnotateImagesResponse;
import com.google.maps.android.clustering.ClusterItem;
import com.qiaoqiao.R;
import com.qiaoqiao.app.App;
import com.qiaoqiao.core.camera.CameraContract;
import com.qiaoqiao.core.camera.CameraPresenter;
import com.qiaoqiao.core.camera.awareness.AwarenessContract;
import com.qiaoqiao.core.camera.awareness.AwarenessPresenter;
import com.qiaoqiao.core.camera.awareness.map.PlaceWrapper;
import com.qiaoqiao.core.camera.awareness.ui.SnapshotPlaceInfoFragment;
import com.qiaoqiao.core.camera.awareness.ui.SnapshotPlacesFragment;
import com.qiaoqiao.core.camera.crop.CropCallback;
import com.qiaoqiao.core.camera.crop.CropContract;
import com.qiaoqiao.core.camera.crop.CropPresenter;
import com.qiaoqiao.core.camera.crop.ui.CropFragment;
import com.qiaoqiao.core.camera.history.HistoryContract;
import com.qiaoqiao.core.camera.history.HistoryPresenter;
import com.qiaoqiao.core.camera.vision.MoreVisionPresenter;
import com.qiaoqiao.core.camera.vision.VisionContract;
import com.qiaoqiao.core.camera.vision.VisionPresenter;
import com.qiaoqiao.core.camera.vision.annotation.target.More;
import com.qiaoqiao.core.camera.vision.annotation.target.Single;
import com.qiaoqiao.core.detail.ui.DetailActivity;
import com.qiaoqiao.customtabs.CustomTabUtils;
import com.qiaoqiao.databinding.ActivityCameraBinding;
import com.qiaoqiao.repository.backend.model.wikipedia.geo.Geosearch;
import com.qiaoqiao.repository.web.ui.WebLinkActivity;
import com.qiaoqiao.utils.DeviceUtils;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.os.Bundle.EMPTY;
import static com.qiaoqiao.core.camera.awareness.ui.SnapshotPlacesFragment.REQ_SETTING_LOCATING;
import static com.qiaoqiao.repository.web.ui.WebLinkActivity.REQ_WEB_LINK;

public final class CameraActivity extends AppCompatActivity implements CameraContract.View,
                                                                       View.OnClickListener,
                                                                       EasyPermissions.PermissionCallbacks,
                                                                       AppBarLayout.OnOffsetChangedListener,
                                                                       FragmentManager.OnBackStackChangedListener,
                                                                       CropCallback {
	private static final int LAYOUT = R.layout.activity_camera;
	private static final int REQUEST_FILE_SELECTOR = 0x19;
	private @Nullable Snackbar mSnackbar;
	private ActivityCameraBinding mBinding;
	private boolean mOnBottom;


	@Inject CropPresenter mCropPresenter;
	@Inject CameraPresenter mCameraPresenter;
	@Inject VisionPresenter mVisionPresenter;
	@Inject HistoryPresenter mHistoryPresenter;
	@Inject AwarenessPresenter mAwarenessPresenter;
	@Inject MoreVisionPresenter mMoreVisionPresenter;

	@Inject @Single VisionContract.View mVisionFragment;
	@Inject @More VisionContract.View moreVisionFragment;
	@Inject HistoryContract.View mHistoryFragment;
	@Inject AwarenessContract.View mSnapshotPlacesFragment;
	@Inject CropContract.View mCropFragment;

	//------------------------------------------------
	//Subscribes, event-handlers
	//------------------------------------------------

	/**
	 * Handler for {@link com.qiaoqiao.core.camera.awareness.bus.OpenClusterItemEvent}.
	 *
	 * @param e Event {@link com.qiaoqiao.core.camera.awareness.bus.OpenClusterItemEvent}.
	 */
	@Subscribe
	public void onEvent(com.qiaoqiao.core.camera.awareness.bus.OpenClusterItemEvent e) {
		ClusterItem clusterItem = e.getClusterItem();
		if (clusterItem instanceof Geosearch) {
			DetailActivity.showInstance(this, ((Geosearch) clusterItem).getPageId());
			return;
		}
		if (clusterItem instanceof PlaceWrapper) {
			SnapshotPlaceInfoFragment.newInstance(this, (PlaceWrapper) clusterItem)
			                         .show(getSupportFragmentManager(), null);
		}
	}
	//------------------------------------------------

	/**
	 * Show single instance of {@link CameraActivity}
	 *
	 * @param cxt {@link Activity}.
	 */
	public static void showInstance(@NonNull Activity cxt) {
		Intent intent = new Intent(cxt, CameraActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
		ActivityCompat.startActivity(cxt, intent, EMPTY);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		MapsInitializer.initialize(this);
		mBinding = DataBindingUtil.setContentView(this, LAYOUT);
		setupAppBar();
		mBinding.appbar.addOnOffsetChangedListener(this);
		App.inject(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		CustomTabUtils.HELPER.bindCustomTabsService(this);
		EventBus.getDefault()
		        .register(this);
	}

	@Override
	protected void onPause() {
		CustomTabUtils.HELPER.unbindCustomTabsService(this);
		EventBus.getDefault()
		        .unregister(this);
		super.onPause();
	}

	@Override
	protected void onStart() {
		super.onStart();
		getSupportFragmentManager().addOnBackStackChangedListener(this);
	}

	@Override
	protected void onStop() {
		getSupportFragmentManager().removeOnBackStackChangedListener(this);
		super.onStop();
	}

	@Override
	public void onBackStackChanged() {
		supportInvalidateOptionsMenu();
	}

	@Inject
	void onInjected() {
		//Views(Fragments), presenters of vision, history are already created but they should be shown on screen.
		setupViewPager((Fragment) mVisionFragment, (Fragment) moreVisionFragment, (Fragment) mHistoryFragment);
		presentersBegin();
	}

	private void setupAppBar() {
		mBinding.appbar.getLayoutParams().height = (int) Math.ceil(DeviceUtils.getScreenSize(this).Height * 0.618f);
		setSupportActionBar(mBinding.toolbar);
		final ActionBar supportActionBar = getSupportActionBar();
		if (supportActionBar != null) {
			supportActionBar.setHomeButtonEnabled(true);
			supportActionBar.setDisplayShowTitleEnabled(false);
		}

		CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) mBinding.appbar.getLayoutParams();
		AppBarLayout.Behavior behavior = new AppBarLayout.Behavior();
		behavior.setDragCallback(new AppBarLayout.Behavior.DragCallback() {
			@Override
			public boolean canDrag(AppBarLayout appBarLayout) {
				if (appBarLayout == null) {
					return true;
				}
				//MapView, CameraView , CoordinatorLayout and CollapsingToolbarLayout make scroll-effect difficultly , let's use a solution.
				//http://stackoverflow.com/questions/31046147/android-mapview-in-a-collapsingtoolbarlayout
				return false;
			}
		});
		params.setBehavior(behavior);
	}


	private void presentersBegin() {
		mCameraPresenter.begin();
		mVisionPresenter.begin();
		mHistoryPresenter.begin();
		mAwarenessPresenter.begin();
		mMoreVisionPresenter.begin();
	}

	@Override
	protected void onDestroy() {
		mBinding.appbar.removeOnOffsetChangedListener(this);
		presentersEnd();
		super.onDestroy();
	}

	private void presentersEnd() {
		mCameraPresenter.end();
		mVisionPresenter.end();
		mHistoryPresenter.end();
		mAwarenessPresenter.end();
		mMoreVisionPresenter.end();
	}


	@SuppressLint("RestrictedApi")
	@Override
	public void showInputFromWeb(@NonNull android.view.View v) {
		WebLinkActivity.showInstance(this, v);
	}

	@Override
	public void showLoadFromLocal(@NonNull android.view.View v) {
		requireExternalStoragePermission();
	}


	@Override
	public void setPresenter(@NonNull CameraPresenter presenter) {
		mCameraPresenter = presenter;
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
			case REQ_SETTING_LOCATING:
				mAwarenessPresenter.locating(this);
				break;
			case REQ_WEB_LINK:
				if (data != null && data.getData() != null) {
					mCameraPresenter.openLink(data.getData());
				}
				break;
			case REQUEST_FILE_SELECTOR:
				if (!(resultCode == Activity.RESULT_OK && data != null && data.getData() != null)) {
					super.onActivityResult(requestCode, resultCode, data);
					return;
				}
				mCameraPresenter.openLocal(getApplicationContext(), data.getData());
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void showError(@NonNull String errorMessage) {
		mSnackbar = Snackbar.make(mBinding.root, errorMessage, Snackbar.LENGTH_LONG)
		                    .setAction(android.R.string.ok, this);
		mSnackbar.show();
	}

	@Override
	public void onClick(View v) {
		if (mSnackbar == null) {
			return;
		}
		mSnackbar.dismiss();
	}


	private void openLocalDir() {
		Intent openPhotoIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		startActivityForResult(openPhotoIntent, REQUEST_FILE_SELECTOR);
	}

	private void openPlaces() {
		getSupportFragmentManager().beginTransaction()
		                           .setCustomAnimations(R.anim.slide_in_from_right, R.anim.slide_out_to_left, android.R.anim.slide_in_left, android.R.anim.slide_out_right)
		                           .add(R.id.camera_container_fl,
		                                (Fragment) mSnapshotPlacesFragment,
		                                mSnapshotPlacesFragment.getClass()
		                                                       .getName())
		                           .addToBackStack(null)
		                           .commit();
	}

	private void openCropView(@NonNull  byte[] data) {
		mCropPresenter.setImageData(data);
		getSupportFragmentManager().beginTransaction()
		                           .add(R.id.camera_container_fl,
		                                (Fragment) mCropFragment,
		                                mCropFragment.getClass()
		                                             .getName())
		                           .addToBackStack(null)
		                           .commit();
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
	}

	@Override
	public void openCrop(@NonNull  byte[] data) {
		openCropView(data);
	}

	@Override
	public void addResponseToScreen(@NonNull BatchAnnotateImagesResponse response) {
		mVisionPresenter.addResponseToScreen(response);
		mMoreVisionPresenter.clean();
		mMoreVisionPresenter.addResponseToScreen(response);


		closeCropView();


		//Select first page ("VISION") and scroll view to top.
		mBinding.appbar.setExpanded(false, true);
		mBinding.viewpager.setCurrentItem(0, true);

	}

	private void closeCropView() {
		boolean isCropThere = ((CropFragment) mCropFragment).isAdded();
		if (isCropThere) {
			getSupportFragmentManager().popBackStack();
		}
	}

	@Override
	public void onCropped(@NonNull byte[] bytes) {
		mCameraPresenter.openCroppedCapturedImage(bytes);
	}

	@Override
	public void onCroppedFail() {
		closeCropView();
	}

	@Override
	public ActivityCameraBinding getBinding() {
		return mBinding;
	}

	@Override
	public void capturePhoto(@NonNull android.view.View v) {
		mBinding.mainControl.startCaptureProgressBar();
		mBinding.camera.takePicture();
	}

	@Override
	public void openLink() {
		mBinding.mainControl.startWebProgressBar();
	}

	@Override
	public void openLocal() {
		mBinding.mainControl.startLocalProgressBar();
	}


	@Override
	public void cameraBegin(@NonNull CameraView.Callback callback) {
		mBinding.camera.addCallback(callback);
		mBinding.camera.start();
	}

	@Override
	public void cameraEnd(@NonNull CameraView.Callback callback) {
		mBinding.camera.removeCallback(callback);
		mBinding.camera.stop();
	}

	@Override
	public void updateWhenResponse() {
		mBinding.mainControl.stopCaptureProgressBar();
		mBinding.mainControl.stopLocalProgressBar();
		mBinding.mainControl.stopWebProgressBar();
		mBinding.barTitleLoadingPb.stopShimmerAnimation();
	}

	private void setupViewPager(@NonNull Fragment visionFragment, @NonNull Fragment moreVisionFragment, @NonNull Fragment historyFragment) {
		ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
		adapter.addFragment(visionFragment, getString(R.string.tab_vision));
		adapter.addFragment(moreVisionFragment, getString(R.string.tab_more));
		adapter.addFragment(historyFragment, getString(R.string.tab_history));
		mBinding.viewpager.setAdapter(adapter);
		mBinding.viewpager.setOffscreenPageLimit(2);
		mBinding.tabs.setupWithViewPager(mBinding.viewpager);
	}

	@Override
	public void updateWhenRequest() {
		mVisionPresenter.setRefreshing(true);
		mMoreVisionPresenter.setRefreshing(true);
		mBinding.barTitleLoadingPb.startShimmerAnimation();
	}

	@Override
	public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
		mOnBottom = (Math.abs(verticalOffset) == appBarLayout.getTotalScrollRange());
	}

	@Override
	public void onBackPressed() {
		if (mOnBottom) {
			mBinding.viewpager.setCurrentItem(0, true);
			mBinding.appbar.setExpanded(true, true);
			return;
		}

		super.onBackPressed();
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_camera, menu);
		return true;
	}


	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		boolean isSnapshotPlacesThere = ((SnapshotPlacesFragment) mSnapshotPlacesFragment).isAdded();
		menu.findItem(R.id.action_places)
		    .setVisible(!isSnapshotPlacesThere);
		@ColorInt int colorTealLight = ResourcesCompat.getColor(getResources(), R.color.colorTealLight, null);
		@ColorInt int colorYellow = ResourcesCompat.getColor(getResources(), R.color.colorYellow, null);
		mBinding.barTitleTv.setTextColor(isSnapshotPlacesThere ?
		                                 colorTealLight :
		                                 colorYellow);

		//When user doesn't crop anything just back, we need stop progressbar on main-control.
		boolean isCropThere = ((CropFragment) mCropFragment).isAdded();
		if (isCropThere) {
			mBinding.mainControl.stopCaptureProgressBar();
		}
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.action_places:
				requireFineLocationPermission();
				break;
		}
		return super.onOptionsItemSelected(item);
	}

	//--Begin permission--

	private static final int RC_EXTERNAL_STORAGE_PERMISSIONS = 123;
	private static final int RC_FINE_LOCATION_PERMISSIONS = 124;

	@SuppressLint("InlinedApi")
	@AfterPermissionGranted(RC_EXTERNAL_STORAGE_PERMISSIONS)
	private void requireExternalStoragePermission() {
		if (EasyPermissions.hasPermissions(this, READ_EXTERNAL_STORAGE)) {
			openLocalDir();
		} else {
			// Ask for one permission
			EasyPermissions.requestPermissions(this, getString(R.string.permission_relation_to_read_external_storage_text), RC_EXTERNAL_STORAGE_PERMISSIONS, READ_EXTERNAL_STORAGE);
		}
	}

	@SuppressLint("InlinedApi")
	@AfterPermissionGranted(RC_FINE_LOCATION_PERMISSIONS)
	private void requireFineLocationPermission() {
		if (EasyPermissions.hasPermissions(this, ACCESS_FINE_LOCATION)) {
			openPlaces();
		} else {
			// Ask for one permission
			EasyPermissions.requestPermissions(this, getString(R.string.permission_relation_to_location_text), RC_FINE_LOCATION_PERMISSIONS, ACCESS_FINE_LOCATION);
		}
	}


	@Override
	public void onPermissionsGranted(int i, List<String> list) {
		if (list.contains(Manifest.permission.READ_EXTERNAL_STORAGE)) {
			openLocalDir();
		}
//		if (list.contains(Manifest.permission.ACCESS_FINE_LOCATION)) {
//			openPlaces();
//		}
	}


	@Override
	public void onPermissionsDenied(int i, List<String> list) {
		if (EasyPermissions.somePermissionPermanentlyDenied(this, Arrays.asList(Manifest.permission.READ_EXTERNAL_STORAGE))) {
			new AppSettingsDialog.Builder(this).setPositiveButton(R.string.permission_setting)
			                                   .build()
			                                   .show();
			return;
		}
		if (EasyPermissions.somePermissionPermanentlyDenied(this, Arrays.asList(Manifest.permission.ACCESS_FINE_LOCATION))) {
			new AppSettingsDialog.Builder(this).setPositiveButton(R.string.permission_setting)
			                                   .build()
			                                   .show();
		}
	}


	//--End permission--
}
