package com.qiaoqiao.core.detail;


import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;

import com.qiaoqiao.repository.DsLoadedCallback;
import com.qiaoqiao.repository.DsRepository;
import com.qiaoqiao.repository.backend.model.wikipedia.LangLink;
import com.qiaoqiao.repository.backend.model.wikipedia.WikiResult;

import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public final class DetailPresenter implements DetailContract.Presenter {
	private final @NonNull DetailContract.View mView;
	private final @NonNull DsRepository mDsRepository;
	private final @NonNull CompositeDisposable mCompositeDisposable = new CompositeDisposable();

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
		public void onException(@NotNull Throwable e) {
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

	private void addToAutoDispose(Disposable... disposables) {
		mCompositeDisposable.addAll(disposables);
	}

	private void autoDispose() {
		mCompositeDisposable.clear();
	}


	@Override
	public void begin(@NonNull FragmentActivity hostActivity) {
		mView.loadDetail();
	}

	@Override
	public void end(@NonNull FragmentActivity hostActivity) {
		autoDispose();
	}

	@Override
	public void loadDetail(int pageId) {
		addToAutoDispose(mDsRepository.onKnowledgeQuery(pageId, mLoadedCallback));
	}

	@Override
	public void loadDetail(LangLink langLink) {
		addToAutoDispose(mDsRepository.onKnowledgeQuery(langLink, mLoadedCallback));
	}


	@Override
	public void loadDetail(String text) {
		addToAutoDispose(mDsRepository.onKnowledgeQuery(text, mLoadedCallback));
	}
}
