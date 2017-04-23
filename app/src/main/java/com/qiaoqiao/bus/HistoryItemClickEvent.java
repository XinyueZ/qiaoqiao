package com.qiaoqiao.bus;

import com.qiaoqiao.ds.database.HistoryItem;

public final class HistoryItemClickEvent {
	private HistoryItem mHistoryItem;

	public void setHistoryItem(HistoryItem historyItem) {
		mHistoryItem = historyItem;
	}

	HistoryItem getHistoryItem() {
		return mHistoryItem;
	}
}
