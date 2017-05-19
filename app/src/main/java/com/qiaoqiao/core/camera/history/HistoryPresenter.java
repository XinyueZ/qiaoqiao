package com.qiaoqiao.core.camera.history;


import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;

import com.qiaoqiao.repository.database.HistoryItem;
import com.qiaoqiao.core.camera.history.bus.HistoryItemClickEvent;

import javax.inject.Inject;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

public final class HistoryPresenter implements HistoryContract.Presenter {
	private final @NonNull HistoryContract.View mView;
	private @Nullable RealmResults<HistoryItem> mResult;

	//------------------------------------------------
	//Subscribes, event-handlers
	//------------------------------------------------

	/**
	 * Handler for {@link HistoryItemClickEvent}.
	 *
	 * @param e Event {@link HistoryItemClickEvent}.
	 */
	@Subscribe
	public void onEvent(HistoryItemClickEvent e) {
		Snackbar.make(mView.getBinding()
		                   .getRoot(), "HistoryItemClickEvent", Snackbar.LENGTH_SHORT)
		        .show();
	}

	//------------------------------------------------


	@Inject
	HistoryPresenter(@NonNull HistoryContract.View view) {
		mView = view;
	}

	@Inject
	void onInjected() {
		mView.setPresenter(this);
	}

	private final RealmChangeListener<RealmResults<HistoryItem>> mChangeListener = new RealmChangeListener<RealmResults<HistoryItem>>() {
		@Override
		public void onChange(RealmResults<HistoryItem> historyItemList) {
			mView.updateList(historyItemList);
		}
	};

	@Override
	public void begin() {
		EventBus.getDefault()
		        .register(this);
	}

	@Override
	public void end() {
		EventBus.getDefault()
		        .unregister(this);
		if (mResult != null) {
			mResult.removeChangeListener(mChangeListener);
		}
	}

	@Override
	public void onViewReady() {
		mResult = Realm.getDefaultInstance()
		               .where(HistoryItem.class)
		               .findAllAsync();
		mView.showList(mResult);
		mResult.addChangeListener(mChangeListener);
	}
}
