package com.qiaoqiao.repository.backend.model.wikipedia;

import java.util.List;

public final class Pages {
	private List<Page> mList;

	public Pages(List<Page> list) {
		mList = list;
	}

	public List<Page> getList() {
		return mList;
	}

	public void setList(List<Page> list) {
		mList = list;
	}
}
