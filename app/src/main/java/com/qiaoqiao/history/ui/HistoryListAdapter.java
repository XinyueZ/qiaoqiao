package com.qiaoqiao.history.ui;


import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.qiaoqiao.R;
import com.qiaoqiao.databinding.ItemHistoryBinding;
import com.qiaoqiao.ds.history.HistoryItem;
import com.qiaoqiao.history.bus.HistoryItemClickEvent;
import com.qiaoqiao.utils.DeviceUtils;

import de.greenrobot.event.EventBus;
import io.realm.RealmResults;

public final class HistoryListAdapter extends RecyclerView.Adapter<HistoryListAdapter.ViewHolder> {
	private static final int ITEM_LAYOUT = R.layout.item_history;
	private final @NonNull RealmResults<HistoryItem> mList;
	private final int mColumns;

	HistoryListAdapter(@NonNull RealmResults<HistoryItem> mList, int columns) {
		this.mList = mList;
		mColumns = columns;
	}

	@Override
	public int getItemCount() {
		return mList.size();
	}

	private HistoryItem getItem(int pos) {
		return mList.get(pos);
	}

	@Override
	public long getItemId(int pos) {
		return pos;
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		Context cxt = parent.getContext();
		int size = DeviceUtils.getScreenSize(cxt).Width / mColumns;
		return new ViewHolder(DataBindingUtil.inflate(LayoutInflater.from(cxt), ITEM_LAYOUT, parent, false), size);
	}

	@Override
	public void onBindViewHolder(ViewHolder holder, int position) {
		HistoryItem historyItem = getItem(position);
		Context cxt = holder.binding.getRoot()
		                            .getContext();
		holder.binding.historyItemIv.getLayoutParams().width = holder.size;
		holder.binding.historyItemIv.getLayoutParams().height = holder.size;
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
		holder.binding.setViewholder(holder);
		holder.binding.setHistoryItem(historyItem);
		holder.binding.executePendingBindings();
	}


	public static final class ViewHolder extends RecyclerView.ViewHolder {
		private final HistoryItemClickEvent historyItemClickEvent = new HistoryItemClickEvent();
		private ItemHistoryBinding binding;
		private int size;

		ViewHolder(ItemHistoryBinding binding, int size) {
			super(binding.getRoot());
			this.binding = binding;
			this.size = size;
		}

		public void onEntryClicked(@NonNull HistoryItem historyItem) {
			historyItemClickEvent.setHistoryItem(historyItem);
			EventBus.getDefault()
			        .post(historyItemClickEvent);
		}
	}
}
