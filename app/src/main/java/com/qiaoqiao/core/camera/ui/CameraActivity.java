package com.qiaoqiao.core.camera.ui;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.afollestad.materialcamera.internal.BaseCaptureActivity;
import com.google.android.gms.appinvite.AppInviteInvitation;
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
import com.qiaoqiao.core.camera.crop.model.CropSource;
import com.qiaoqiao.core.camera.crop.ui.CropFragment;
import com.qiaoqiao.core.camera.history.HistoryCallback;
import com.qiaoqiao.core.camera.history.HistoryContract;
import com.qiaoqiao.core.camera.history.HistoryPresenter;
import com.qiaoqiao.core.camera.history.HistoryPresenter2;
import com.qiaoqiao.core.camera.vision.VisionContract;
import com.qiaoqiao.core.camera.vision.VisionPresenter;
import com.qiaoqiao.core.confidence.ConfidenceContract;
import com.qiaoqiao.core.confidence.ConfidencePresenter;
import com.qiaoqiao.core.confidence.ui.ConfidenceDialogFragment;
import com.qiaoqiao.core.detail.ui.DetailActivity;
import com.qiaoqiao.customtabs.CustomTabUtils;
import com.qiaoqiao.databinding.ActivityCameraBinding;
import com.qiaoqiao.licenses.LicensesActivity;
import com.qiaoqiao.repository.backend.model.wikipedia.geo.Geosearch;
import com.qiaoqiao.repository.web.ui.WebLinkActivity;
import com.qiaoqiao.settings.SettingsActivity;
import com.qiaoqiao.utils.AppUtils;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.afollestad.materialcamera.internal.CameraIntentKey.STILL_SHOT;
import static com.qiaoqiao.core.camera.awareness.AwarenessPresenterKt.REQ_SETTING_LOCATING;
import static com.qiaoqiao.repository.web.ui.WebLinkActivityKt.REQ_WEB_LINK;
import static com.qiaoqiao.settings.PermissionRcKt.RC_FINE_LOCATION_PERMISSIONS;
import static com.qiaoqiao.settings.PermissionRcKt.RC_READ_EXTERNAL_STORAGE_PERMISSIONS;

public abstract class CameraActivity extends BaseCaptureActivity implements CameraContract.View,
                                                                            View.OnClickListener,
                                                                            EasyPermissions.PermissionCallbacks,
                                                                            AppBarLayout.OnOffsetChangedListener,
                                                                            FragmentManager.OnBackStackChangedListener,
                                                                            CropCallback,
                                                                            NavigationView.OnNavigationItemSelectedListener,
                                                                            HistoryCallback {
	private static final int LAYOUT = R.layout.activity_camera;
	private static final int REQ_FILE_SELECTOR = 0x19;
	private static final int REQ_INVITE = 0x56;
	private static final int REQ_CAMERA = 0x79;

	private @Nullable Snackbar mSnackbar;
	private ActivityCameraBinding mBinding;
	private boolean mOnBottom;
	private ActionBarDrawerToggle mDrawerToggle;

	@Inject CropPresenter mCropPresenter;
	@Inject CameraPresenter mCameraPresenter;
	@Inject VisionPresenter mVisionPresenter;
	@Inject HistoryPresenter mHistoryPresenter;
	@Inject HistoryPresenter2 mHistoryPresenter2;
	@Inject AwarenessPresenter mAwarenessPresenter;
	@Inject ConfidencePresenter mConfidencePresenter;

	@Inject CropContract.View mCropFragment;
	@Inject VisionContract.View mVisionFragment;
	@Inject HistoryContract.View mHistoryFragment;
	@Inject HistoryContract.View2 mHistoryFragment2;
	@Inject ConfidenceContract.View mConfidenceFragment;
	@Inject AwarenessContract.View mSnapshotPlacesFragment;

	//------------------------------------------------
	//Subscribes, event-handlers
	//------------------------------------------------

	/**
	 * Handler for {@link com.qiaoqiao.core.camera.awareness.bus.OpenClusterItemEvent}.
	 *
	 * @param e Event {@link com.qiaoqiao.core.camera.awareness.bus.OpenClusterItemEvent}.
	 */
	@SuppressWarnings("unused")
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


	public static void showInstance(@NonNull Activity cxt, boolean shotPoto) {
		if (shotPoto) {
			new Camera(cxt).stillShot()
			               .showPortraitWarning(false)
			               .start(REQ_CAMERA);
		} else {
			new Camera(cxt).showPortraitWarning(false)
			               .start(REQ_CAMERA);
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		MapsInitializer.initialize(this);
		setupDataBinding();
		setupAppBar();
		setupNavigationDrawer();
		App.inject(this);
	}


	@Override
	protected int getLayout() {
		return LAYOUT;
	}

	private void setupDataBinding() {
		mBinding = DataBindingUtil.bind(findViewById(R.id.drawer_layout));
		mBinding.setClickHandler(this);
	}

	@Inject
	void onInjected() {
		//Views(Fragments), presenters of vision, history are already created but they should be shown on screen.
		setupViewPager((Fragment) mVisionFragment, (Fragment) mHistoryFragment);
		presentersBegin();
	}


	private void setupNavigationDrawer() {
		ActionBar actionBar = getSupportActionBar();
		if (actionBar == null) {
			return;
		}
		actionBar.setHomeButtonEnabled(true);
		actionBar.setDisplayHomeAsUpEnabled(true);
		mDrawerToggle = new ActionBarDrawerToggle(this, mBinding.drawerLayout, R.string.app_name, R.string.app_name) {
			@Override
			public void onDrawerOpened(View drawerView) {
			}
		};
		mBinding.drawerLayout.addDrawerListener(mDrawerToggle);
		mBinding.navView.setNavigationItemSelectedListener(this);
	}

	private void setDownNavigationDrawer() {
		if (mDrawerToggle != null) {
			mBinding.drawerLayout.removeDrawerListener(mDrawerToggle);
		}
	}

	@Override
	protected void onResume() {
		super.onResume();

		CustomTabUtils.HELPER.bindCustomTabsService(this);
		EventBus.getDefault()
		        .register(this);

		if (mDrawerToggle != null) {
			mDrawerToggle.syncState();
		}
	}


	private void setupCamera() {
//		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
//		boolean autoFocus = prefs.getBoolean(getString(R.string.preference_key_camera_auto_focus_allowed), true);
//		mBinding.camera.setAutoFocus(autoFocus);
//		int flash = Integer.valueOf(prefs.getString(getString(R.string.preference_key_camera_flash_options), "3"));
//		mBinding.camera.setFlash(flash);
//		String aspectRatio = prefs.getString(getString(R.string.preference_key_camera_aspect_ratio), "4:3");
//		mBinding.camera.setAspectRatio(AspectRatio.parse(aspectRatio));
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


	private void setupAppBar() {
		mBinding.appbar.addOnOffsetChangedListener(this);
		showCameraOnly();
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
			public boolean canDrag(@NonNull AppBarLayout appBarLayout) {

				//MapView, CameraView , CoordinatorLayout and CollapsingToolbarLayout make scroll-effect difficultly , let's use a solution.
				//http://stackoverflow.com/questions/31046147/android-mapview-in-a-collapsingtoolbarlayout
				return false;
			}
		});
		params.setBehavior(behavior);
	}

	private void setDownAppBar() {
		mBinding.appbar.removeOnOffsetChangedListener(this);
	}

	private void showCameraOnly() {
		mBinding.appbar.setExpanded(true, true);
	}


	private void showVisionOnly() {
		mBinding.appbar.setExpanded(false, true);
		mBinding.expandMoreBtn.setVisibility(GONE);
		mBinding.expandLessBtn.setVisibility(VISIBLE);
	}

	private void toggleVisionCameraShowButtons() {
		if (!mOnBottom) {
			mBinding.expandMoreBtn.setVisibility(VISIBLE);
			mBinding.expandLessBtn.setVisibility(GONE);
		} else {
			mBinding.expandMoreBtn.setVisibility(GONE);
			mBinding.expandLessBtn.setVisibility(VISIBLE);
		}
	}

	private void presentersBegin() {
		mCropPresenter.begin(this);
		mCameraPresenter.begin(this);
		mVisionPresenter.begin(this);
		mHistoryPresenter.begin(this);
		mHistoryPresenter2.begin(this);
		mHistoryPresenter2.setCropPresenter(mCropPresenter);
		mAwarenessPresenter.begin(this);
		mConfidencePresenter.begin(this);
	}

	@Override
	protected void onDestroy() {
		setDownNavigationDrawer();
		setDownAppBar();
		presentersEnd();
		super.onDestroy();
	}

	private void presentersEnd() {
		mCropPresenter.end(this);
		mCameraPresenter.end(this);
		mVisionPresenter.end(this);
		mHistoryPresenter.end(this);
		mHistoryPresenter2.end(this);
		mAwarenessPresenter.end(this);
		mConfidencePresenter.end(this);
	}

	@Override
	public void showInputFromWeb(@NonNull android.view.View v) {
		WebLinkActivity.Companion.showInstance(this, v);
	}

	@Override
	public void showLoadFromLocal(@NonNull android.view.View v) {
		requireReadExternalStoragePermission();
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
			case REQ_INVITE:
				if (resultCode != RESULT_OK) {
					mSnackbar = Snackbar.make(mBinding.root, R.string.invitation_send_failed, Snackbar.LENGTH_LONG)
					                    .setAction(android.R.string.cancel, this);
					mSnackbar.show();
				}
				break;
			case REQ_FILE_SELECTOR:
				if (!(resultCode == Activity.RESULT_OK && data != null && data.getData() != null)) {
					super.onActivityResult(requestCode, resultCode, data);
					return;
				}
				mCameraPresenter.openLocal(getApplicationContext(), data.getData());
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	protected void onUseMedia(boolean isStillshot, @NonNull Intent intent) {
		super.onUseMedia(isStillshot, intent);
		final Uri data = intent.getData();
		openCrop(new CropSource(null, data));
//		onRetry(data.toString());
	}

	@Override
	public void showError(@NonNull String errorMessage) {
		mSnackbar = Snackbar.make(mBinding.root, errorMessage, Snackbar.LENGTH_LONG)
		                    .setAction(android.R.string.ok, this);
		mSnackbar.show();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.expand_less_btn:
				showCameraOnly();
				break;
			case R.id.expand_more_btn:
				showVisionOnly();
				break;
			default:
				if (mSnackbar == null) {
					return;
				}
				mSnackbar.dismiss();

		}
	}


	private void openLocalDir() {
		Intent openPhotoIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		startActivityForResult(openPhotoIntent, REQ_FILE_SELECTOR);
	}

	private void openPlaces() {
		getSupportFragmentManager().beginTransaction()
		                           .setCustomAnimations(R.anim.slide_in_from_right, R.anim.slide_out_to_left, android.R.anim.slide_in_left, android.R.anim.slide_out_right)
		                           .add(R.id.container,
		                                (Fragment) mSnapshotPlacesFragment,
		                                mSnapshotPlacesFragment.getClass()
		                                                       .getName())
		                           .addToBackStack(null)
		                           .commit();
	}

	private void openCropView(@NonNull CropSource cropSource) {
		mCropPresenter.setCropSource(cropSource);
		getSupportFragmentManager().beginTransaction()
		                           .add(R.id.container,
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
	public void openCrop(@NonNull CropSource cropSource) {
		openCropView(cropSource);
	}

	@Override
	@Subscribe
	public void addResponseToScreen(@NonNull BatchAnnotateImagesResponse response) {
		mVisionPresenter.addResponseToScreen(response);

		closeCropView();

		//Select first page ("VISION") and scroll view to top.
		showVisionOnly();
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
		mCameraPresenter.findAnnotateImages(bytes);
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
	public void updateWhenResponse() {
		mBinding.barTitleLoadingPb.stopShimmerAnimation();
	}

	private void setupViewPager(@NonNull Fragment visionFragment, @NonNull Fragment historyFragment) {
		ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(), new ArrayList<Fragment>() {{
			add(visionFragment);
			add(historyFragment);
		}}, new ArrayList<String>() {{
			add(getString(R.string.tab_vision));
			add(getString(R.string.tab_history));
		}});
		mBinding.viewpager.setAdapter(adapter);
		mBinding.tabs.setupWithViewPager(mBinding.viewpager);
	}

	@Override
	public void updateWhenRequest() {
		mVisionPresenter.setRefreshing(true);
		mBinding.barTitleLoadingPb.startShimmerAnimation();
	}

	@Override
	public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
		mOnBottom = (Math.abs(verticalOffset) == appBarLayout.getTotalScrollRange());
		toggleVisionCameraShowButtons();
	}

	@Override
	public void onBackPressed() {
		if (mBinding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
			mBinding.drawerLayout.closeDrawer(GravityCompat.START);
			return;
		}

		if (mOnBottom) {
			mBinding.viewpager.setCurrentItem(0, true);
			showCameraOnly();
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
		adjustUIForDifferentFragmentSenario(menu);
		return super.onPrepareOptionsMenu(menu);
	}

	private void adjustUIForDifferentFragmentSenario(Menu menu) {
		boolean isStillshot = getIntent().getBooleanExtra(STILL_SHOT, true);
		boolean isSnapshotPlacesThere = ((SnapshotPlacesFragment) mSnapshotPlacesFragment).isAdded();
		boolean isCropThere = ((CropFragment) mCropFragment).isAdded();

		mBinding.navView.getMenu()
		                .findItem(R.id.action_places)
		                .setVisible(!isSnapshotPlacesThere);
		mBinding.navView.getMenu()
		                .findItem(R.id.action_from_local)
		                .setVisible(!isSnapshotPlacesThere);
		mBinding.navView.getMenu()
		                .findItem(R.id.action_from_web)
		                .setVisible(!isSnapshotPlacesThere);

		menu.findItem(R.id.action_crop_rotate)
		    .setVisible(isCropThere && !isSnapshotPlacesThere);
		menu.findItem(R.id.action_video)
		    .setVisible(!isCropThere && !isSnapshotPlacesThere && isStillshot);
		menu.findItem(R.id.action_photo)
		    .setVisible(!isCropThere && !isSnapshotPlacesThere && !isStillshot);

		ViewCompat.animate(mBinding.expandMoreBtn)
		          .alpha(!isCropThere && !isSnapshotPlacesThere ?
		                 1 :
		                 0)
		          .start();
		ViewCompat.animate(mBinding.expandLessBtn)
		          .alpha(!isCropThere && !isSnapshotPlacesThere ?
		                 1 :
		                 0)
		          .start();
		final View view = getSupportFragmentManager().findFragmentById(R.id.stackview_history_fg)
		                                             .getView();
		ViewCompat.animate(view)
		          .alpha(!isCropThere && !isSnapshotPlacesThere ?
		                 1 :
		                 0)
		          .start();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (mDrawerToggle != null && mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}

		switch (item.getItemId()) {
			case R.id.action_video:
				CameraActivity.showInstance(this, false);
				finish();
				break;
			case R.id.action_photo:
				CameraActivity.showInstance(this, true);
				finish();
				break;
			case R.id.action_crop_rotate:
				mCropFragment.rotate();
				break;
		}
		return super.onOptionsItemSelected(item);
	}


	@Override
	public boolean onNavigationItemSelected(@NonNull MenuItem item) {
		mBinding.drawerLayout.closeDrawers();
		switch (item.getItemId()) {
			case R.id.action_from_local:
				showLoadFromLocal(null);
				break;
			case R.id.action_from_web:
				showInputFromWeb(null);
				break;
			case R.id.action_places:
				requireFineLocationPermission();
				break;
			case R.id.action_confidence:
				((ConfidenceDialogFragment) mConfidenceFragment).show(getSupportFragmentManager(), null);
				break;
			case R.id.action_app_invite:
				sendAppInvitation();
				break;
			case R.id.action_settings:
				SettingsActivity.showInstance(this);
				break;
			case R.id.action_source_license:
				LicensesActivity.Companion.showInstance(this);
				break;
		}
		return true;
	}


	@Override
	public void cleared() {
		mVisionPresenter.clear();
	}


	private void sendAppInvitation() {
		String invitationTitle = getString(R.string.invitation_title);
		String invitationMessage = getString(R.string.invitation_message);
		String invitationDeepLink = getString(R.string.invitation_link);
		String invitationCustomImage = getString(R.string.invitation_image);
		String invitationCta = getString(R.string.invitation_cta);


		Intent intent = new AppInviteInvitation.IntentBuilder(invitationTitle).setMessage(invitationMessage)
		                                                                      .setDeepLink(Uri.parse(invitationDeepLink))
		                                                                      .setCustomImage(Uri.parse(invitationCustomImage))
		                                                                      .setCallToActionText(invitationCta)
		                                                                      .build();
		try {
			startActivityForResult(intent, REQ_INVITE);
		} catch (ActivityNotFoundException | NullPointerException ex) {
			new AlertDialog.Builder(this).setTitle(R.string.invitation_title)
			                             .setMessage(R.string.invitation_error)
			                             .setPositiveButton(android.R.string.ok, (dialog, whichButton) -> {
				                             dialog.dismiss();
				                             AppUtils.goToPlayServiceDownload(getApplicationContext());
			                             })
			                             .setCancelable(true)
			                             .create()
			                             .show();
		}
	}


	//--Begin permission--


	@AfterPermissionGranted(RC_READ_EXTERNAL_STORAGE_PERMISSIONS)
	private void requireReadExternalStoragePermission() {
		if (EasyPermissions.hasPermissions(this, READ_EXTERNAL_STORAGE)) {
			openLocalDir();
		} else {
			// Ask for one permission
			EasyPermissions.requestPermissions(this, getString(R.string.permission_relation_to_read_external_storage_text), RC_READ_EXTERNAL_STORAGE_PERMISSIONS, READ_EXTERNAL_STORAGE);
		}
	}


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
	}


	@Override
	public void onPermissionsDenied(int i, List<String> perms) {
		if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
			new AppSettingsDialog.Builder(this).setPositiveButton(R.string.permission_setting)
			                                   .build()
			                                   .show();
		}
	}


	//--End permission--
}
