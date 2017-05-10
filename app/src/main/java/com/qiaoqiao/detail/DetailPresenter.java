package com.qiaoqiao.detail;


import android.support.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.qiaoqiao.backend.model.wikipedia.WikiResult;
import com.qiaoqiao.ds.AbstractDsSource;
import com.qiaoqiao.ds.DsRepository;
import com.qiaoqiao.utils.LL;

import javax.inject.Inject;

public final class DetailPresenter implements DetailContract.Presenter {
	private final @NonNull DetailContract.View mView;
	private final @NonNull DsRepository mDsRepository;
	private final @NonNull String mKeyword;

	@Inject
	DetailPresenter(@NonNull DetailContract.View view, @NonNull DsRepository dsRepository, @NonNull String keyword) {
		mView = view;
		mDsRepository = dsRepository;
		mKeyword = keyword;
	}

	@Inject
	void onInjected() {
		mView.setPresenter(this);
	}

	@Override
	public void begin() {
		LL.d("DetailPresenter: begin()");
		showDetail(mKeyword);
	}

	@Override
	public void end() {
		//Still not impl.
	}


	private void showDetail(String text) {
		mDsRepository.onKnowledgeQuery(text, new AbstractDsSource.LoadedCallback() {
			@Override
			public void onKnowledgeResponse(WikiResult result) {
				super.onKnowledgeResponse(result);
				loadImage(result.getQuery()
				                .getPages()
				                .getList()
				                .get(0)
				                .getThumbnail()
				                .getSource(),
				          result.getQuery()
				                .getPages()
				                .getList()
				                .get(0)
				                .getOriginal()
				                .getSource());

				mView.getBinding().collapsingToolbar.setTitle(result.getQuery()
				                                                    .getPages()
				                                                    .getList()
				                                                    .get(0)
				                                                    .getTitle());
				mView.getBinding().contentTv.setText(result.getQuery()
				                                           .getPages()
				                                           .getList()
				                                           .get(0)
				                                           .getExtract());
			}
		});
	}

	private void loadImage(@NonNull String previewUrl, @NonNull final String url) {
		Glide.with(mView.getBinding()
		                .getFragment()
		                .getContext())
		     .load(previewUrl)
		     .listener(new RequestListener<String, GlideDrawable>() {
			     @Override
			     public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
				     Glide.with(mView.getBinding()
				                     .getFragment()
				                     .getContext())
				          .load(url)
				          .centerCrop()
				          .crossFade()
				          .into(mView.getBinding().detailIv);
				     return true;
			     }

			     @Override
			     public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
				     return false;
			     }

		     })
		     .centerCrop()
		     .crossFade()
		     .into(mView.getBinding().detailIv);
	}
}
