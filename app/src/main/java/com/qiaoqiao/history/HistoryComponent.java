package com.qiaoqiao.history;

import com.qiaoqiao.app.ActivityScoped;
import com.qiaoqiao.ds.DsRepositoryComponent;

import dagger.Component;

@ActivityScoped
@Component(dependencies = DsRepositoryComponent.class, modules = HistoryModule.class)
public interface HistoryComponent {
	HistoryManager getHistoryManager();
}
