package com.qiaoqiao.core.detail;


import android.support.annotation.NonNull;

import com.qiaoqiao.repository.DsLoadedCallback;
import com.qiaoqiao.repository.DsRepository;
import com.qiaoqiao.repository.backend.model.wikipedia.LangLink;
import com.qiaoqiao.repository.backend.model.wikipedia.WikiResult;

import javax.inject.Inject;

public final class DetailPresenter implements DetailContract.Presenter {
	private final @NonNull DetailContract.View mView;
	private final @NonNull DsRepository mDsRepository;
	private final DsLoadedCallback mLoadedCallback = new DsLoadedCallback() {
		@Override
		public void onKnowledgeResponse(WikiResult result) {
			super.onKnowledgeResponse(result);
			mView.setDetailImages(result.getQuery()
			                            .getPages()
			                            .getList()
			                            .get(0)
			                            .getThumbnail(),
			                      result.getQuery()
			                      .getPages()
			                      .getList()
			                      .get(0)
			                      .getOriginal());

			mView.setMultiLanguage(result.getQuery()
			                             .getPages()
			                             .getList()
			                             .get(0)
			                             .getLangLinks());

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
		}

		@Override
		public void onException(@NonNull Exception e) {
			super.onException(e);
			mView.onError();
		}
	};

	@Inject
	DetailPresenter(@NonNull DetailContract.View view, @NonNull DsRepository dsRepository) {
		mView = view;
		mDsRepository = dsRepository;
	}

	@Inject
	void onInjected() {
		mView.setPresenter(this);
	}

	@Override
	public void begin() {
		mView.loadDetail();
	}

	@Override
	public void end() {
		//Still not impl.
	}

	@Override
	public void loadDetail(int pageId) {
		mDsRepository.onKnowledgeQuery(pageId, mLoadedCallback);
	}

	@Override
	public void loadDetail(LangLink langLink) {
		mDsRepository.onKnowledgeQuery(langLink, mLoadedCallback);
	}


	@Override
	public void loadDetail(String text) {
		mDsRepository.onKnowledgeQuery(text, mLoadedCallback);
	}
}
