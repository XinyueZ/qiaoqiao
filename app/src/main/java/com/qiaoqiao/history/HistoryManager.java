package com.qiaoqiao.history;


import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;

import com.qiaoqiao.bus.HistoryItemClickEvent;
import com.qiaoqiao.databinding.FragmentHistoryBinding;
import com.qiaoqiao.ds.database.HistoryItem;
import com.qiaoqiao.history.ui.HistoryStackAdapter;

import javax.inject.Inject;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

public final class HistoryManager implements HistoryContract.Presenter {
	private final @NonNull FragmentHistoryBinding mBinding;
	private final @NonNull HistoryContract.View mView;
	private @Nullable RealmResults<HistoryItem> mResult;
	private @Nullable HistoryStackAdapter mHistoryStackAdapter;

	//------------------------------------------------
	//Subscribes, event-handlers
	//------------------------------------------------

	/**
	 * Handler for {@link HistoryItemClickEvent}.
	 * @param e Event {@link HistoryItemClickEvent}.
	 */
	@Subscribe
	public void onEvent(HistoryItemClickEvent e) {
		Snackbar.make(mBinding.getRoot(), "HistoryItemClickEvent", Snackbar.LENGTH_SHORT).show();
	}

	//------------------------------------------------
	@Inject
	HistoryManager(@NonNull FragmentHistoryBinding binding, @NonNull HistoryContract.View view) {
		mBinding = binding;
		mView = view;
	}

	@Inject
	void onInjected() {
		mView.setPresenter(this);
	}

	private final RealmChangeListener<RealmResults<HistoryItem>> mChangeListener = new RealmChangeListener<RealmResults<HistoryItem>>() {
		@Override
		public void onChange(RealmResults<HistoryItem> historyItem) {
			if (mHistoryStackAdapter == null) {
				return;
			}
			mHistoryStackAdapter.notifyDataSetChanged();
			mBinding.historyStv.setSelection(mHistoryStackAdapter.getCount() - 1);
		}
	};

	@Override
	public void start() {
		mResult = Realm.getDefaultInstance()
		               .where(HistoryItem.class)
		               .findAllAsync();
		mBinding.historyStv.setAdapter(mHistoryStackAdapter = new HistoryStackAdapter(mBinding.getFragment()
		                                                                                      .getContext(), mResult));
		if (mResult != null) {
			mResult.addChangeListener(mChangeListener);
		}
		EventBus.getDefault()
		        .register(this);
	}

	@Override
	public void stop() {
		EventBus.getDefault()
		        .unregister(this);
		if (mResult != null) {
			mResult.removeChangeListener(mChangeListener);
		}
	}
}
