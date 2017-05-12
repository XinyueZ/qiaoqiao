package com.qiaoqiao.camera.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;

import com.google.api.services.vision.v1.model.BatchAnnotateImagesResponse;
import com.qiaoqiao.R;
import com.qiaoqiao.app.App;
import com.qiaoqiao.camera.CameraContract;
import com.qiaoqiao.camera.CameraModule;
import com.qiaoqiao.camera.CameraPresenter;
import com.qiaoqiao.camera.DaggerCameraComponent;
import com.qiaoqiao.databinding.ActivityCameraBinding;
import com.qiaoqiao.ds.web.bus.WebLinkInputEvent;
import com.qiaoqiao.ds.web.ui.FromInputWebLinkFragment;
import com.qiaoqiao.history.DaggerHistoryComponent;
import com.qiaoqiao.history.HistoryModule;
import com.qiaoqiao.history.ui.HistoryFragment;
import com.qiaoqiao.utils.SystemUiHelper;
import com.qiaoqiao.vision.DaggerVisionComponent;
import com.qiaoqiao.vision.VisionPresenter;
import com.qiaoqiao.vision.VisionModule;
import com.qiaoqiao.vision.ui.VisionListFragment;

import java.util.List;

import javax.inject.Inject;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.os.Bundle.EMPTY;

public final class CameraActivity extends AppCompatActivity implements CameraContract.View,
                                                                       View.OnClickListener,
                                                                       EasyPermissions.PermissionCallbacks {
	private static final int LAYOUT = R.layout.activity_camera;
	private static final int REQUEST_FILE_SELECTOR = 0x19;
	private @Nullable Snackbar mSnackbar;
	private VisionPresenter mVisionPresenter;
	private ActivityCameraBinding mBinding;
	@Inject CameraPresenter mPresenter;

	//------------------------------------------------
	//Subscribes, event-handlers
	//------------------------------------------------

	/**
	 * Handler for {@link WebLinkInputEvent}.
	 *
	 * @param e Event {@link WebLinkInputEvent}.
	 */
	@Subscribe
	public void onEvent(WebLinkInputEvent e) {
		mPresenter.openLink(e.getUri());
		getSupportFragmentManager().popBackStack();
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
		SystemUiHelper uiHelper = new SystemUiHelper(this, SystemUiHelper.LEVEL_IMMERSIVE, 0);
		uiHelper.hide();
		super.onCreate(savedInstanceState);
		mBinding = DataBindingUtil.setContentView(this, LAYOUT);
		mBinding.setUiHelper(uiHelper);
		mBinding.setDecorView((ViewGroup) getWindow().getDecorView());

		DaggerCameraComponent.builder()
		                     .dsRepositoryComponent(((App) getApplication()).getRepositoryComponent())
		                     .cameraModule(new CameraModule(this))
		                     .build()
		                     .injectCamera(this);
		mVisionPresenter = DaggerVisionComponent.builder()
		                                        .dsRepositoryComponent(((App) getApplication()).getRepositoryComponent())
		                                        .visionModule(new VisionModule((VisionListFragment) getSupportFragmentManager().findFragmentById(R.id.vision_fg)))
		                                        .build()
		                                        .getVisionManager();
		DaggerHistoryComponent.builder()
		                      .dsRepositoryComponent(((App) getApplication()).getRepositoryComponent())
		                      .historyModule(new HistoryModule((HistoryFragment) getSupportFragmentManager().findFragmentById(R.id.history_fg)))
		                      .build()
		                      .getHistoryManager();

	}


	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		if (mBinding != null) {
			mBinding.getUiHelper()
			        .hide();
		}
		super.onWindowFocusChanged(hasFocus);
	}

	@SuppressLint("RestrictedApi")
	@Override
	public void showInputFromWeb() {
		getSupportFragmentManager().beginTransaction()
		                           .setCustomAnimations(R.anim.slide_in_from_right, R.anim.slide_out_to_left, android.R.anim.slide_in_left, android.R.anim.slide_out_right)
		                           .replace(R.id.content_root_fl, FromInputWebLinkFragment.newInstance(this))
		                           .addToBackStack(null)
		                           .commit();
	}

	@Override
	public void showLoadFromLocal() {
		requirePermissions();
	}

	@Override
	protected void onResume() {
		EventBus.getDefault()
		        .register(this);
		super.onResume();
		mPresenter.begin();
	}

	@Override
	protected void onPause() {
		EventBus.getDefault()
		        .unregister(this);
		mPresenter.stop();
		super.onPause();
	}

	@Override
	public void setPresenter(@NonNull CameraPresenter presenter) {
		mPresenter = presenter;
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
			case REQUEST_FILE_SELECTOR:
				if (!(resultCode == Activity.RESULT_OK && data != null && data.getData() != null)) {
					super.onActivityResult(requestCode, resultCode, data);
					return;
				}
				mPresenter.openLocal(getApplicationContext(), data.getData());
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void showError(@NonNull android.view.View view, @NonNull String errorMessage) {
		mSnackbar = Snackbar.make(view, errorMessage, Snackbar.LENGTH_LONG)
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


	private static final int RC_PERMISSIONS = 123;


	@SuppressLint("InlinedApi")
	@AfterPermissionGranted(RC_PERMISSIONS)
	private void requirePermissions() {
		if (hasPermissions()) {
			openLocalDir();
		} else {
			// Ask for one permission
			EasyPermissions.requestPermissions(this, getString(R.string.permission_relation_to_read_external_storage_text), RC_PERMISSIONS, READ_EXTERNAL_STORAGE);
		}
	}


	@SuppressLint("InlinedApi")
	private boolean hasPermissions() {
		return EasyPermissions.hasPermissions(this, READ_EXTERNAL_STORAGE);
	}


	@Override
	public void onPermissionsDenied(int i, List<String> list) {
		if (!hasPermissions()) {
			new AppSettingsDialog.Builder(this).setPositiveButton(R.string.permission_setting)
			                                   .setNegativeButton(getString(R.string.exit_app), new DialogInterface.OnClickListener() {
				                                   @Override
				                                   public void onClick(DialogInterface dialogInterface, int i) {
					                                   supportFinishAfterTransition();
				                                   }
			                                   })
			                                   .build()
			                                   .show();
		} else {
			openLocalDir();
		}
	}

	@Override
	public void onPermissionsGranted(int i, List<String> list) {
		openLocalDir();
	}

	private void openLocalDir() {
		Intent openPhotoIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		startActivityForResult(openPhotoIntent, REQUEST_FILE_SELECTOR);
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
	}

	@Override
	public void addResponseToScreen(@NonNull BatchAnnotateImagesResponse response) {
		if (mVisionPresenter != null) {
			mVisionPresenter.addResponseToScreen(response);
		}
	}

	@Override
	public ActivityCameraBinding getBinding() {
		return mBinding;
	}
}
