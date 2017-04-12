package com.qiaoqiao.home;


import android.content.Context;
import android.content.res.Resources;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import com.google.android.cameraview.CameraView;
import com.qiaoqiao.databinding.HomeBinding;
import com.qiaoqiao.ds.DsRepository;
import com.qiaoqiao.ds.DsSource;
import com.qiaoqiao.utils.LL;
import com.qiaoqiao.views.MainControlView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.inject.Inject;

import static android.os.Build.VERSION_CODES.JELLY_BEAN;
import static com.qiaoqiao.app.Values.TEMP_PHOTO_NAME;
import static com.qiaoqiao.ds.DsType.CAMERA;
import static com.qiaoqiao.ds.DsType.LOCAL;
import static com.qiaoqiao.ds.DsType.WEB;

public final class Home implements HomeContract.Presenter {
	private final @NonNull Context mContext;
	private final @NonNull HomeBinding mBinding;
	private final @NonNull HomeContract.View mView;
	private final @NonNull DsRepository mDsRepository;

	@Inject
	Home(@NonNull Context cxt, @NonNull HomeContract.View view, @NonNull HomeBinding binding, @NonNull DsRepository dsRepository) {
		mContext = cxt;
		mView = view;
		mBinding = binding;
		mDsRepository = dsRepository;
	}

	@Inject
	void onInjected() {
		mBinding.camera.addCallback(mCameraCallback);
		mBinding.mainControl.setOnFromLocalClickedListener(new MainControlView.OnFromLocalClickedListener() {
			@Override
			public void onClick(View v) {
				mView.showLoadFromLocal();
				mBinding.getUiHelper()
				        .hide();
//				mBinding.loadingPb.setVisibility(View.VISIBLE);
			}
		});
		mBinding.mainControl.setOnCaptureClickedListener(new MainControlView.OnCaptureClickedListener() {
			@Override
			public void onClick(View v) {
				capturePhoto();
				mBinding.getUiHelper()
				        .hide();
//				mBinding.loadingPb.setVisibility(View.VISIBLE);
			}
		});
		mBinding.mainControl.setOnFromWebClickedListener(new MainControlView.OnFromWebClickedListener() {
			@Override
			public void onClick(View v) {
				mView.showInputFromWeb();
				mBinding.getUiHelper()
				        .hide();
//				mBinding.loadingPb.setVisibility(View.VISIBLE);
			}
		});

		mView.setPresenter(this);
	}

	@Override
	public void start() {
		mBinding.camera.start();
		mBinding.getDecorView()
		        .getViewTreeObserver()
		        .addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
			        @Override
			        public void onGlobalLayout() {
				        final ViewGroup decorView = mBinding.getDecorView();
				        if (Build.VERSION.SDK_INT >= JELLY_BEAN) {
					        decorView.getViewTreeObserver()
					                 .removeOnGlobalLayoutListener(this);
				        } else {
					        decorView.getViewTreeObserver()
					                 .removeGlobalOnLayoutListener(this);
				        }
				        Rect rect = new Rect();
				        decorView.getWindowVisibleDisplayFrame(rect);
				        mBinding.home.setPadding(0, rect.top, 0, 0);
			        }
		        });
		final Resources resources = mBinding.getDecorView()
		                                    .getContext()
		                                    .getResources();
		int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
		if (resourceId > 0) {
			((ViewGroup.MarginLayoutParams) mBinding.mainControl.getLayoutParams()).bottomMargin = resources.getDimensionPixelSize(resourceId);
		}
	}

	@Override
	public void stop() {
		mBinding.camera.removeCallback(mCameraCallback);
		mBinding.camera.stop();
	}

	@Override
	public void changeFocus() {
		mBinding.getUiHelper()
		        .hide();
	}

	@Override
	public void capturePhoto() {
		mBinding.camera.takePicture();
	}

	private final CameraView.Callback mCameraCallback = new CameraView.Callback() {
		private Handler mBackgroundHandler;

		private Handler getBackgroundHandler() {
			if (mBackgroundHandler == null) {
				HandlerThread thread = new HandlerThread("background");
				thread.start();
				mBackgroundHandler = new Handler(thread.getLooper());
			}
			return mBackgroundHandler;
		}

		@Override
		public void onPictureTaken(final CameraView cameraView, final byte[] data) {
			getBackgroundHandler().post(new Runnable() {
				@Override
				public void run() {
					File file = new File(mContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES), TEMP_PHOTO_NAME);
					OutputStream os = null;
					try {
						os = new FileOutputStream(file);
						os.write(data);
						os.close();
						mDsRepository.loadData(mContext, CAMERA, new DsSource.DataLoadedCallback() {
							@Override
							public void onLoaded(@NonNull byte[] data) {
								LL.d("Home- onPictureTaken: " + data);
								LL.d("Home- onPictureTaken: " + data.length);
							}

							@Override
							public void on() {

							}

						});
					} catch (IOException e) {
					} finally {
						if (os != null) {
							try {
								os.close();
							} catch (IOException e) {
							}
						}
					}
				}
			});
		}

		@Override
		public void onCameraOpened(CameraView cameraView) {
		}

		@Override
		public void onCameraClosed(CameraView cameraView) {
		}
	};

	@Override
	public void copyLink(@NonNull final Uri uri) {
		mDsRepository.loadData(mContext, WEB, new DsSource.DataLoadedCallback() {
			@Override
			public void on() {
				LL.d("Home- copyLink: " + uri);
			}

			@Override
			public void onLoaded(@NonNull byte[] data) {

			}
		});
	}

	@Override
	public void openLocal(@NonNull final Uri uri) {
		mDsRepository.loadData(mContext, LOCAL, new DsSource.DataLoadedCallback() {
			@Override
			public void onLoaded(@NonNull byte[] data) {
				LL.d("Home- openLocal: " + uri);
			}

			@Override
			public void on() {
			}

		});
	}
}
