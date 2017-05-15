package com.qiaoqiao.history.ui;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.qiaoqiao.R;
import com.qiaoqiao.ds.database.HistoryItem;
import com.qiaoqiao.databinding.FragmentHistoryBinding;
import com.qiaoqiao.history.HistoryContract;
import com.qiaoqiao.history.HistoryPresenter;

import java.util.List;

import io.realm.RealmResults;

public final class HistoryFragment extends Fragment implements HistoryContract.View {
	private static final int LAYOUT = R.layout.fragment_history;
	private FragmentHistoryBinding mBinding;
	private HistoryContract.Presenter mPresenter;

	private @Nullable HistoryListAdapter mHistoryListAdapter;

	public static HistoryFragment newInstance(@NonNull Context cxt) {
		return (HistoryFragment) HistoryFragment.instantiate(cxt, HistoryFragment.class.getName());
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		mBinding = DataBindingUtil.inflate(inflater, LAYOUT, container, false);
		mBinding.setFragment(this);
		return mBinding.getRoot();
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		mPresenter.onViewReady();
	}


	@Override
	public void setPresenter(@NonNull HistoryPresenter presenter) {
		mPresenter = presenter;
	}

	@Override
	public FragmentHistoryBinding getBinding() {
		return mBinding;
	}

	@Override
	public void showList(@NonNull List<HistoryItem> results) {
		final int columns = getResources().getInteger(R.integer.num_columns);
		mBinding.historyRv.setLayoutManager(new GridLayoutManager(getActivity(), columns));
		mBinding.historyRv.setAdapter(mHistoryListAdapter = new HistoryListAdapter(results, columns));
	}

	@Override
	public void updateList(@NonNull RealmResults<HistoryItem> historyItemList) {
		if (mHistoryListAdapter == null) {
			return;
		}
		mHistoryListAdapter.notifyDataSetChanged();
		mBinding.historyRv.setVisibility(historyItemList.size() > 0 ?
		                                 View.VISIBLE :
		                                 View.GONE);
	}
}
