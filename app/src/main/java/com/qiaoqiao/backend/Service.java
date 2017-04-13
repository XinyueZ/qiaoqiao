package com.qiaoqiao.backend;

import com.demo.navigator.ds.model.NavigationEntries;

import io.reactivex.Observable;
import retrofit2.http.GET;

public interface Service {
	@GET("api/navigation")
	Observable<NavigationEntries> getNavigationEntries();


}
