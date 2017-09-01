package com.qiaoqiao.core.camera.ui;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.google.android.gms.maps.MapsInitializer;
import com.google.maps.android.clustering.ClusterItem;
import com.qiaoqiao.R;
import com.qiaoqiao.app.App;
import com.qiaoqiao.core.camera.CameraContract;
import com.qiaoqiao.core.camera.CameraPresenter;
import com.qiaoqiao.core.camera.awareness.AwarenessContract;
import com.qiaoqiao.core.camera.awareness.AwarenessPresenter;
import com.qiaoqiao.core.camera.awareness.map.PlaceWrapper;
import com.qiaoqiao.core.camera.awareness.ui.SnapshotPlaceInfoFragment;
import com.qiaoqiao.core.camera.crop.CropCallback;
import com.qiaoqiao.core.camera.crop.CropContract;
import com.qiaoqiao.core.camera.crop.CropPresenter;
import com.qiaoqiao.core.camera.crop.model.CropSource;
import com.qiaoqiao.core.camera.crop.ui.CropFragment;
import com.qiaoqiao.core.camera.history.HistoryContract;
import com.qiaoqiao.core.camera.history.HistoryPresenter2;
import com.qiaoqiao.core.camera.vision.VisionContract;
import com.qiaoqiao.core.camera.vision.VisionPresenter;
import com.qiaoqiao.core.confidence.ConfidenceContract;
import com.qiaoqiao.core.confidence.ConfidencePresenter;
import com.qiaoqiao.core.detail.ui.DetailActivity;
import com.qiaoqiao.databinding.ActivityCameraBinding;
import com.qiaoqiao.repository.backend.model.wikipedia.geo.Geosearch;
import com.qiaoqiao.views.ViewPagerAdapter;

import java.util.ArrayList;

import javax.inject.Inject;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;

import static android.os.Bundle.EMPTY;
import static android.view.View.GONE;
import static android.view.View.VISIBLE;


public final class CameraActivity extends AppCompatActivity implements CameraContract.View,
                                                                       View.OnClickListener,
                                                                       AppBarLayout.OnOffsetChangedListener,
                                                                       FragmentManager.OnBackStackChangedListener,
                                                                       CropCallback,
                                                                       NavigationView.OnNavigationItemSelectedListener {
	private static final int LAYOUT = R.layout.activity_camera;
	public static final int REQ_FILE_SELECTOR = 0x19;
	private static final String EXTRAS_BACK_CAMERA = CameraActivity.class.getName() + ".EXTRAS.back";

	@Nullable Snackbar mSnackbar;
	ActivityCameraBinding mBinding;
	boolean mOnBottom;
	ActionBarDrawerToggle mDrawerToggle;

	@Inject CropPresenter mCropPresenter;
	@Inject CameraPresenter mCameraPresenter;
	@Inject VisionPresenter mVisionPresenter;
	@Inject HistoryPresenter2 mHistoryPresenter2;
	@Inject AwarenessPresenter mAwarenessPresenter;
	@Inject ConfidencePresenter mConfidencePresenter;

	@Inject CropContract.View mCropFragment;
	@Inject VisionContract.View mVisionFragment;
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
			DetailActivity.Companion.showInstance(this, ((Geosearch) clusterItem).getPageId());
			return;
		}
		if (clusterItem instanceof PlaceWrapper) {
			SnapshotPlaceInfoFragment.Companion.newInstance(this, (PlaceWrapper) clusterItem)
			                                   .show(getSupportFragmentManager(), null);
		}
	}
	//------------------------------------------------

	public static void showInstance(@NonNull Activity cxt, boolean backCamera) {
		Intent intent = new Intent(cxt, CameraActivity.class);
		intent.putExtra(EXTRAS_BACK_CAMERA, backCamera);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		ActivityCompat.startActivity(cxt, intent, EMPTY);
	}

	boolean isBackCamera() {
		return getIntent().getBooleanExtra(EXTRAS_BACK_CAMERA, true);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		MapsInitializer.initialize(this);
		setupDataBinding();
		setupAppBar();
		setupNavigationDrawer();
		App.Companion.inject(this);
		mPermissionHelper.requireCameraPermission();
		getSupportFragmentManager().addOnBackStackChangedListener(this);
	}


	private void setupDataBinding() {
		mBinding = DataBindingUtil.setContentView(this, LAYOUT);
		mBinding.setClickHandler(this);
	}

	@Inject
	void onInjected() {
		//Views(Fragments), presenters of vision, history are already created but they should be shown on screen.
		setupViewPager((Fragment) mVisionFragment);
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

		EventBus.getDefault()
		        .register(this);

		if (mDrawerToggle != null) {
			mDrawerToggle.syncState();
		}
	}


	@Override
	protected void onPause() {
		EventBus.getDefault()
		        .unregister(this);
		super.onPause();
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

	void showCameraOnly() {
		mBinding.appbar.setExpanded(true, true);
	}


	void showVisionOnly() {
		mBinding.appbar.setExpanded(false, true);
		mBinding.expandMoreBtn.setVisibility(GONE);
		mBinding.expandLessBtn.setVisibility(VISIBLE);
	}


	private void presentersBegin() {
		mCropPresenter.begin(this);
		mCameraPresenter.begin(this);
		mCameraPresenter.setVisionPresenter(mVisionPresenter);
		mVisionPresenter.begin(this);
		mHistoryPresenter2.begin(this);
		mHistoryPresenter2.setVisionPresenter(mVisionPresenter);
		mAwarenessPresenter.begin(this);
		mConfidencePresenter.begin(this);
	}

	@Override
	protected void onDestroy() {
		getSupportFragmentManager().removeOnBackStackChangedListener(this);
		setDownNavigationDrawer();
		setDownAppBar();
		presentersEnd();
		super.onDestroy();

		try{
			mBinding.preview.stop();
			mBinding.preview.release();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void presentersEnd() {
		mCropPresenter.end(this);
		mCameraPresenter.end(this);
		mVisionPresenter.end(this);
		mHistoryPresenter2.end(this);
		mAwarenessPresenter.end(this);
		mConfidencePresenter.end(this);
	}

	@Override
	public void showInputFromWeb(@Nullable android.view.View v) {
		WebLinkActivity.Companion.showInstance(this, v);
	}

	@Override
	public void showLoadFromLocal(@Nullable android.view.View v) {
		mPermissionHelper.requireReadExternalStoragePermission();
	}


	@Override
	public void setPresenter(@NonNull CameraPresenter presenter) {
		mCameraPresenter = presenter;
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (Scenario.INSTANCE.onActivityResult(this, requestCode, resultCode, data)) {
			return;
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
		ClickHandler.INSTANCE.onClick(this, v);
	}


	void openLocalDir() {
		startActivityForResult(new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI), REQ_FILE_SELECTOR);
	}


	@Override
	public void openCrop(@NonNull CropSource cropSource) {
		mCropPresenter.setCropSource(cropSource);
		getSupportFragmentManager().beginTransaction()
		                           .add(R.id.container,
		                                (Fragment) mCropFragment,
		                                mCropFragment.getClass()
		                                             .getName())
		                           .addToBackStack(null)
		                           .commitAllowingStateLoss();
	}

	private void closeCropView() {
		if (((CropFragment) mCropFragment).isAdded()) {
			getSupportFragmentManager().popBackStackImmediate();
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
	public void updateViewWhenResponse() {
		mBinding.barTitleLoadingPb.stopShimmerAnimation();
		showVisionOnly();
		if (mBinding.viewpager.getCurrentItem() != 0) {
			mBinding.viewpager.setCurrentItem(0);
		}
		closeCropView();
	}

	private void setupViewPager(@NonNull Fragment visionFragment) {
		ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(), new ArrayList<Fragment>() {{
			add(visionFragment);
		}}, new ArrayList<String>() {{
			add(getString(R.string.tab_vision));
		}});
		mBinding.viewpager.setAdapter(adapter);
		mBinding.tabs.setupWithViewPager(mBinding.viewpager);
	}

	@Override
	public void updateViewWhenRequest() {
		mVisionPresenter.setRefreshing(true);
		mBinding.barTitleLoadingPb.startShimmerAnimation();
	}

	@Override
	public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
		Scenario.INSTANCE.onOffsetChanged(this, appBarLayout, verticalOffset);
	}

	@Override
	public void onBackPressed() {
		if (BackPressHandler.INSTANCE.onBackPressed(this)) {
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
		Scenario.INSTANCE.adjustUIForDifferentFragmentScenario(this, menu);
		return super.onPrepareOptionsMenu(menu);
	}


	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return MenuHandler.INSTANCE.onOptionsItemSelected(this, item) || super.onOptionsItemSelected(item);
	}


	@Override
	public boolean onNavigationItemSelected(@NonNull MenuItem item) {
		MenuHandler.INSTANCE.onNavigationItemSelected(this, item);
		return true;
	}


	//--Begin permission--
	final PermissionHelper mPermissionHelper = new PermissionHelper(this);


	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		mPermissionHelper.onRequestPermissionsResult(requestCode, permissions, grantResults);
	}
	//--End permission--
}
