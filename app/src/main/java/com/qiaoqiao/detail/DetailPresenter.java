package com.qiaoqiao.detail;


import android.support.annotation.NonNull;

import com.qiaoqiao.backend.model.wikipedia.LangLink;
import com.qiaoqiao.backend.model.wikipedia.WikiResult;
import com.qiaoqiao.ds.DsLoadedCallback;
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
		loadDetail(mKeyword);
	}

	@Override
	public void end() {
		//Still not impl.
	}


	@Override
	public void loadDetail(LangLink langLink) {
		mDsRepository.onKnowledgeQuery(langLink, new DsLoadedCallback() {
			@Override
			public void onKnowledgeResponse(WikiResult result) {
				super.onKnowledgeResponse(result);
				mView.setMultiLanguage(result.getQuery()
				                             .getPages()
				                             .getList()
				                             .get(0)
				                             .getLangLinks());
				mView.showImage(result.getQuery()
				                      .getPages()
				                      .getList()
				                      .get(0)
				                      .getThumbnail(),
				                result.getQuery()
				                      .getPages()
				                      .getList()
				                      .get(0)
				                      .getOriginal());
				mView.setText(result.getQuery()
				                    .getPages()
				                    .getList()
				                    .get(0)
				                    .getTitle(),
				              result.getQuery()
				                    .getPages()
				                    .getList()
				                    .get(0)
				                    .getExtract());
				mView.toggleLoaded();
			}
		});
	}


	private void loadDetail(String text) {
		mDsRepository.onKnowledgeQuery(text, new DsLoadedCallback() {
			@Override
			public void onKnowledgeResponse(WikiResult result) {
				super.onKnowledgeResponse(result);
				mView.setMultiLanguage(result.getQuery()
				                             .getPages()
				                             .getList()
				                             .get(0)
				                             .getLangLinks());
				mView.showImage(result.getQuery()
				                      .getPages()
				                      .getList()
				                      .get(0)
				                      .getThumbnail(),
				                result.getQuery()
				                      .getPages()
				                      .getList()
				                      .get(0)
				                      .getOriginal());
				mView.setText(result.getQuery()
				                    .getPages()
				                    .getList()
				                    .get(0)
				                    .getTitle(),
				              result.getQuery()
				                    .getPages()
				                    .getList()
				                    .get(0)
				                    .getExtract());
				mView.toggleLoaded();
			}
		});
	}
}
