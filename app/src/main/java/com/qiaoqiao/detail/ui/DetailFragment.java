package com.qiaoqiao.detail.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.GlideBitmapDrawable;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.qiaoqiao.R;
import com.qiaoqiao.backend.model.wikipedia.Image;
import com.qiaoqiao.backend.model.wikipedia.LangLink;
import com.qiaoqiao.databinding.FragmentDetailBinding;
import com.qiaoqiao.detail.DetailContract;
import com.qiaoqiao.detail.DetailPresenter;
import com.qiaoqiao.utils.DeviceUtils;

import java.lang.ref.WeakReference;

public final class DetailFragment extends Fragment implements DetailContract.View,
                                                              AppBarLayout.OnOffsetChangedListener,
                                                              Palette.PaletteAsyncListener {
	private static final int LAYOUT = R.layout.fragment_detail;
	private DetailContract.Presenter mPresenter;
	private FragmentDetailBinding mBinding;
	private WeakReference<Context> mContextWeakReference;
	private MenuItem mMultiLanguageMenuItem;
	private @Nullable Image mPreviewImage;

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		mBinding = DataBindingUtil.inflate(inflater, LAYOUT, container, false);
		mBinding.setFragment(this);
		setHasOptionsMenu(true);
		return mBinding.getRoot();
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.menu_detail, menu);
		mMultiLanguageMenuItem = menu.findItem(R.id.multi_language_spinner);
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		toggleContentLoading();
		setRefreshing(false);
		int actionBarHeight = calcActionBarHeight(getContext());
		mBinding.loadingPb.setColorSchemeResources(R.color.colorPrimary, R.color.colorPrimaryDark, R.color.colorAccent);
		mBinding.loadingPb.setProgressViewEndTarget(true, actionBarHeight * 2);
		mBinding.loadingPb.setProgressViewOffset(false, 0, actionBarHeight * 2);
		mContextWeakReference = new WeakReference<>(getContext());
		mBinding.appbar.getLayoutParams().height = (int) Math.ceil(DeviceUtils.getScreenSize(getContext()).Height * 0.618f);
		mBinding.content.getSettings()
		                .setDefaultTextEncodingName("utf-8");
		mBinding.content.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
					view.loadUrl(request.getUrl()
					                    .toString());
				}
				return super.shouldOverrideUrlLoading(view, request);
			}

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return super.shouldOverrideUrlLoading(view, url);
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				toggleLoaded();
				super.onPageFinished(view, url);
			}
		});
		mBinding.appbar.addOnOffsetChangedListener(this);

		final String transName = getActivity().getIntent()
		                                      .getStringExtra(getString(R.string.transition_share_item_name));
		if (!TextUtils.isEmpty(transName)) {
			ViewCompat.setTransitionName(mBinding.appbar, transName);
		}
	}

	@Override
	public void onDestroyView() {
		mBinding.appbar.removeOnOffsetChangedListener(this);
		super.onDestroyView();
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		final AppCompatActivity activity = (AppCompatActivity) getActivity();
		activity.setSupportActionBar(mBinding.toolbar);
		if (activity.getSupportActionBar() == null) {
			return;
		}
		activity.getSupportActionBar()
		        .setDisplayHomeAsUpEnabled(true);
	}

	@Override
	public void setPresenter(@NonNull DetailPresenter presenter) {
		mPresenter = presenter;
	}

	@Override
	public void setMultiLanguage(@Nullable LangLink[] langLinks) {
		if (langLinks == null || langLinks.length <= 0) {
			return;
		}
		for (LangLink langLink : langLinks) {
			mMultiLanguageMenuItem.getSubMenu()
			                      .add(langLink.toString())
			                      .setOnMenuItemClickListener(item -> {
				                      mPresenter.loadDetail(langLink);
				                      return true;
			                      });
		}
	}

	@Override
	public FragmentDetailBinding getBinding() {
		return mBinding;
	}

	private void toggleContentLoading() {
		mBinding.layoutLoading.loadingPb.startShimmerAnimation();
	}

	private void toggleLoaded() {
		mBinding.layoutLoading.loadingPb.stopShimmerAnimation();
		mBinding.layoutLoading.loadingPb.setVisibility(View.GONE);
	}

	@Override
	public void showImage(@Nullable Image preview, @Nullable Image photo) {
		if (mContextWeakReference.get() == null || photo == null) {
			return;
		}

		mPreviewImage = preview;
		setRefreshing(true);
		Glide.with(mContextWeakReference.get())
		     .load(photo.getSource())
		     .crossFade()
		     .centerCrop()
		     .diskCacheStrategy(DiskCacheStrategy.ALL)
		     .error(R.drawable.ic_default_image)
		     .placeholder(R.drawable.ic_default_image)
		     .skipMemoryCache(false)
		     .listener(new RequestListener<String, GlideDrawable>() {
			     @Override
			     public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
				     setRefreshing(false);
				     Palette.Builder b = new Palette.Builder(((GlideBitmapDrawable) resource).getBitmap());
				     b.maximumColorCount(1);
				     b.generate(DetailFragment.this);
				     return false;
			     }

			     @Override
			     public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
				     Snackbar.make(mBinding.getRoot(), R.string.no_image, Toast.LENGTH_SHORT)
				             .show();
				     return true;
			     }
		     })
		     .into(mBinding.detailIv);
	}


	@Override
	public void setText(@NonNull String title, @NonNull String content) {
		mBinding.collapsingToolbar.setTitle(title);
		mBinding.content.loadData(content, "text/html; charset=utf-8", "utf-8");
	}

	@Override
	public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
		if (mContextWeakReference.get() == null || mPreviewImage == null) {
			return;
		}
		if (Math.abs(verticalOffset) == appBarLayout.getTotalScrollRange()) {
			Glide.with(mContextWeakReference.get())
			     .load(mPreviewImage.getSource())
			     .crossFade()
			     .centerCrop()
			     .diskCacheStrategy(DiskCacheStrategy.ALL)
			     .skipMemoryCache(false)
			     .placeholder(R.drawable.ic_default_image)
			     .error(R.drawable.ic_default_image)
			     .listener(new RequestListener<String, GlideDrawable>() {
				     @Override
				     public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
					     mBinding.previewIv.setVisibility(View.VISIBLE);
					     return false;
				     }

				     @Override
				     public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
					     Snackbar.make(mBinding.getRoot(), R.string.no_image, Toast.LENGTH_SHORT)
					             .show();
					     return true;
				     }
			     })
			     .into(mBinding.previewIv);
		} else {
			mBinding.previewIv.setVisibility(View.GONE);
		}
	}

	@Override
	public void onGenerated(Palette palette) {
		if (palette.getSwatches()
		           .isEmpty()) {
			return;
		}
		int textColor = palette.getSwatches()
		                       .get(0)
		                       .getBodyTextColor();
		int barColor = palette.getSwatches()
		                      .get(0)
		                      .getRgb();
		mBinding.collapsingToolbar.setExpandedTitleColor(textColor);
		mBinding.collapsingToolbar.setCollapsedTitleTextColor(textColor);
		mBinding.collapsingToolbar.setContentScrimColor(barColor);
		mBinding.collapsingToolbar.setStatusBarScrimColor(barColor);
	}

	private static int calcActionBarHeight(Context cxt) {
		int[] abSzAttr;
		abSzAttr = new int[] { android.R.attr.actionBarSize };
		TypedArray a = cxt.obtainStyledAttributes(abSzAttr);
		return a.getDimensionPixelSize(0, -1);
	}

	public void setRefreshing(boolean refresh) {
		mBinding.loadingPb.setEnabled(refresh);
		mBinding.loadingPb.setRefreshing(refresh);
	}

	@Override
	public void onError() {
		Snackbar.make(mBinding.getRoot(), R.string.loading_detail_fail, Snackbar.LENGTH_INDEFINITE).setAction(android.R.string.ok, v -> getActivity().supportFinishAfterTransition()).show();
	}
}
