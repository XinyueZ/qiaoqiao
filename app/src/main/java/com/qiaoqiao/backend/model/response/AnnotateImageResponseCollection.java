package com.qiaoqiao.backend.model.response;


import com.google.gson.annotations.SerializedName;

import java.util.List;

public final class AnnotateImageResponseCollection {
	@SerializedName("responses") private List<AnnotateImageResponse> mAnnotateImageResponses;


	public AnnotateImageResponseCollection(List<AnnotateImageResponse> annotateImageResponses) {
		mAnnotateImageResponses = annotateImageResponses;
	}


	public List<AnnotateImageResponse> getAnnotateImageResponses() {
		return mAnnotateImageResponses;
	}

	public void setAnnotateImageResponses(List<AnnotateImageResponse> annotateImageResponses) {
		mAnnotateImageResponses = annotateImageResponses;
	}
}
