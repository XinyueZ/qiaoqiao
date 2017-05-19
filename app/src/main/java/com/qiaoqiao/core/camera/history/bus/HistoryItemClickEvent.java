package com.qiaoqiao.core.camera.history.bus;

import com.qiaoqiao.repository.database.HistoryItem;

public final class HistoryItemClickEvent {
	private HistoryItem mHistoryItem;

	public void setHistoryItem(HistoryItem historyItem) {
		mHistoryItem = historyItem;
	}

	HistoryItem getHistoryItem() {
		return mHistoryItem;
	}
}
