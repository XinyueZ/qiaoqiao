package com.qiaoqiao.backend.model.request;


import com.google.gson.annotations.SerializedName;

public final class AnnotateImageRequestCollection {
	@SerializedName("requests") private AnnotateImageRequest[] mAnnotateImageRequests;

	public AnnotateImageRequestCollection(AnnotateImageRequest... annotateImageRequests) {
		mAnnotateImageRequests = annotateImageRequests;
	}

	public AnnotateImageRequest[] getAnnotateImageRequests() {
		return mAnnotateImageRequests;
	}

	public void setAnnotateImageRequests(AnnotateImageRequest[] annotateImageRequests) {
		mAnnotateImageRequests = annotateImageRequests;
	}
}
