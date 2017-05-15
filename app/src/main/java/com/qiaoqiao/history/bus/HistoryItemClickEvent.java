package com.qiaoqiao.history.bus;

import com.qiaoqiao.database.HistoryItem;

public final class HistoryItemClickEvent {
	private HistoryItem mHistoryItem;

	public void setHistoryItem(HistoryItem historyItem) {
		mHistoryItem = historyItem;
	}

	HistoryItem getHistoryItem() {
		return mHistoryItem;
	}
}
