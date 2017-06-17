package com.qiaoqiao.core.camera.history;


import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;

import com.qiaoqiao.repository.database.HistoryItem;

import javax.inject.Inject;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

public final class HistoryPresenter2 implements HistoryContract.Presenter2 {
	private final @NonNull HistoryContract.View2 mView;
	private @Nullable RealmResults<HistoryItem> mResult;
	private @NonNull HistoryCallback mHistoryCallback;


	@Inject
	HistoryPresenter2(@NonNull HistoryContract.View2 view, @NonNull HistoryCallback historyCallback) {
		mView = view;
		mHistoryCallback = historyCallback;
	}

	@Inject
	void onInjected() {
		mView.setPresenter(this);
	}

	private final RealmChangeListener<RealmResults<HistoryItem>> mChangeListener = new RealmChangeListener<RealmResults<HistoryItem>>() {
		@Override
		public void onChange(RealmResults<HistoryItem> historyItemList) {
			mView.updateList(historyItemList);
			if (historyItemList.size() == 0) {
				mHistoryCallback.cleared();
			}
		}
	};

	@Override
	public void begin(@NonNull FragmentActivity hostActivity) {
		loadHistory();
	}

	@Override
	public void end(@NonNull FragmentActivity hostActivity) {
		if (mResult != null) {
			mResult.removeChangeListener(mChangeListener);
		}
	}

	@Override
	public void loadHistory() {
		mResult = Realm.getDefaultInstance()
		               .where(HistoryItem.class)
		               .findAllAsync();
		mView.showList(mResult);
		mResult.addChangeListener(mChangeListener);
	}
}
