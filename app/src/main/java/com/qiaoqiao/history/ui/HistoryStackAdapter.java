package com.qiaoqiao.history.ui;


import android.content.Context;
import android.databinding.DataBindingUtil;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.bumptech.glide.Glide;
import com.qiaoqiao.R;
import com.qiaoqiao.databinding.ItemHistoryBinding;
import com.qiaoqiao.ds.database.HistoryItem;

import io.realm.RealmResults;

public final class HistoryStackAdapter extends BaseAdapter {
	private static final int ITEM_LAYOUT = R.layout.item_history;
	private final RealmResults<HistoryItem> mList;
	private final LayoutInflater mLayoutInflater;

	public HistoryStackAdapter(Context context, RealmResults<HistoryItem> mList) {
		this.mList = mList;
		this.mLayoutInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return mList.size();
	}

	@Override
	public HistoryItem getItem(int pos) {
		return mList.get(pos);
	}

	@Override
	public long getItemId(int pos) {
		return pos;
	}

	@Override
	public View getView(int pos, View view, ViewGroup parent) {
		ViewHolder holder;
		HistoryItem historyItem = mList.get(pos);
		if (view == null) {
			holder = new ViewHolder();
			holder.binding = DataBindingUtil.inflate(mLayoutInflater, ITEM_LAYOUT, parent, false);
			view = holder.binding.getRoot();
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		Context cxt = holder.binding.getRoot()
		                            .getContext();
		if (historyItem.getByteArray() != null && historyItem.getByteArray().length > 0) {
			Glide.with(cxt)
			     .load(historyItem.getByteArray())
			     .centerCrop()
			     .placeholder(R.drawable.ic_default_image)
			     .crossFade()
			     .into(holder.binding.historyItemIv);
		} else {
			if (TextUtils.isEmpty(historyItem.getImageUri())) {
				holder.binding.historyItemIv.setImageResource(R.drawable.ic_default_image);
			} else {
				Glide.with(cxt)
				     .load(historyItem.getImageUri())
				     .centerCrop()
				     .placeholder(R.drawable.ic_default_image)
				     .crossFade()
				     .into(holder.binding.historyItemIv);
			}
		}
		holder.binding.executePendingBindings();
		return view;
	}

	private final class ViewHolder {
		ItemHistoryBinding binding;
	}
}
