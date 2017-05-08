package com.qiaoqiao.detail;


import android.support.annotation.NonNull;
import android.widget.Toast;

import com.qiaoqiao.backend.model.translate.Data;
import com.qiaoqiao.backend.model.translate.TranslateTextResponseTranslation;
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
		mDsRepository.onTranslate(mKeyword, new AbstractDsSource.LoadedCallback() {
			@Override
			public void onTranslateData(@NonNull Data translateData) {
				super.onTranslateData(translateData);
				final TranslateTextResponseTranslation[] translations = translateData.getTranslations();
				final TranslateTextResponseTranslation translation = translations[0];
				Toast.makeText(mView.getBinding()
				                    .getFragment()
				                    .getContext(), translation.getTranslatedText(), Toast.LENGTH_LONG)
				     .show();
			}
		});
	}

	@Override
	public void end() {
		//Still not impl.
	}
}
