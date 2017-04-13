package com.qiaoqiao.backend.model.request;


import com.google.gson.annotations.SerializedName;

import java.util.List;

public final class AnnotateImageRequestCollection {
	@SerializedName("requests") private List<AnnotateImageRequest> mAnnotateImageRequests;

	public AnnotateImageRequestCollection(List<AnnotateImageRequest> annotateImageRequests) {
		mAnnotateImageRequests = annotateImageRequests;
	}

	public List<AnnotateImageRequest> getAnnotateImageRequests() {
		return mAnnotateImageRequests;
	}

	public void setAnnotateImageRequests(List<AnnotateImageRequest> annotateImageRequests) {
		mAnnotateImageRequests = annotateImageRequests;
	}
}
