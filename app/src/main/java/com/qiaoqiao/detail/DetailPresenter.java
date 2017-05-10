package com.qiaoqiao.detail;


import android.support.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.qiaoqiao.backend.model.wikipedia.WikiResult;
import com.qiaoqiao.ds.AbstractDsSource;
import com.qiaoqiao.ds.DsRepository;

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
				Glide.with(mView.getBinding()
				                .getFragment()
				                .getContext())
				     .load(result.getQuery()
				                 .getPages()
				                 .getList()
				                 .get(0)
				                 .getOriginal()
				                 .getSource())
				     .centerCrop()
				     .crossFade()
				     .into(mView.getBinding().detailIv);

				mView.getBinding().titleTv.setText(result.getQuery()
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
}
