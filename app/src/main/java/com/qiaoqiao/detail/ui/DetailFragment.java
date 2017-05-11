package com.qiaoqiao.detail.ui;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.qiaoqiao.R;
import com.qiaoqiao.databinding.FragmentDetailBinding;
import com.qiaoqiao.detail.DetailContract;
import com.qiaoqiao.detail.DetailPresenter;
import com.qiaoqiao.utils.DeviceUtils;

public final class DetailFragment extends Fragment implements DetailContract.View,
                                                              AppBarLayout.OnOffsetChangedListener {
	private static final int LAYOUT = R.layout.fragment_detail;
	private DetailPresenter mPresenter;
	private FragmentDetailBinding mBinding;
	private String mScrollBackgroundUrl;

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		mBinding = DataBindingUtil.inflate(inflater, LAYOUT, container, false);
		mBinding.setFragment(this);
		return mBinding.getRoot();
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		mBinding.appbar.getLayoutParams().height = DeviceUtils.getScreenSize(getContext()).Height / 2;
		mBinding.content.getSettings()
		                .setDefaultTextEncodingName("utf-8");
		mBinding.appbar.addOnOffsetChangedListener(this);
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		final AppCompatActivity activity = (AppCompatActivity) getActivity();
		activity.setSupportActionBar(mBinding.toolbar);
		activity.getSupportActionBar()
		        .setDisplayHomeAsUpEnabled(true);
	}

	@Override
	public void setPresenter(@NonNull DetailPresenter presenter) {
		mPresenter = presenter;
	}

	@Override
	public FragmentDetailBinding getBinding() {
		return mBinding;
	}

	@Override
	public void onStart() {
		super.onStart();
		mPresenter.begin();
	}

	@Override
	public void onStop() {
		super.onStop();
		mPresenter.end();
	}

	@Override
	public void toggleLoaded() {
		mBinding.layoutLoading.getRoot()
		                      .setVisibility(View.GONE);
	}

	@Override
	public void showImage(@NonNull String previewUrl, @NonNull final String url) {
		mScrollBackgroundUrl = previewUrl;
		Glide.with(getContext())
		     .load(url)
		     .crossFade()
		     .centerCrop()
		     .diskCacheStrategy(DiskCacheStrategy.ALL)
		     .skipMemoryCache(false)
		     .listener(new RequestListener<String, GlideDrawable>() {
			     @Override
			     public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
				     mBinding.loadingPb.setVisibility(View.GONE);
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
		if (Math.abs(verticalOffset) == appBarLayout.getTotalScrollRange()) {
			Glide.with(getContext())
			     .load(mScrollBackgroundUrl)
			     .crossFade()
			     .centerCrop()
			     .diskCacheStrategy(DiskCacheStrategy.ALL)
			     .skipMemoryCache(false)
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
}
