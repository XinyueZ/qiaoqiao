package com.qiaoqiao.history;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;

import com.qiaoqiao.R;
import com.qiaoqiao.databinding.FragmentHistoryBinding;
import com.qiaoqiao.ds.database.HistoryItem;
import com.qiaoqiao.history.ui.HistoryStackAdapter;

import javax.inject.Inject;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

public final class HistoryManager implements HistoryContract.Presenter,
                                             AdapterView.OnItemSelectedListener {
	private final @NonNull FragmentHistoryBinding mBinding;
	private final @NonNull HistoryContract.View mView;
	private @Nullable RealmResults<HistoryItem> mResult;
	private @Nullable HistoryStackAdapter mHistoryStackAdapter;

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
		mBinding.historyStv.setOnItemSelectedListener(this);
	}

	@Override
	public void stop() {
		if (mResult != null) {
			mResult.removeChangeListener(mChangeListener);
		}
		mBinding.historyStv.setOnItemSelectedListener(null);
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
		Context cxt = view.getContext();
		mBinding.historyItemTv.setText(String.format(cxt.getString(R.string.page), String.valueOf(position + 1)));
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {

	}
}
