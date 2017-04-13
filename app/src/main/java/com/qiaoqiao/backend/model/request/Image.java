package com.qiaoqiao.backend.model.request;


import com.google.gson.annotations.SerializedName;

public final class Image {
	@SerializedName("content") private byte[] content;
	@SerializedName("source") private ImageSource source;

	public Image(byte[] content, ImageSource source) {
		this.content = content;
		this.source = source;
	}

	public byte[] getContent() {
		return content;
	}

	public void setContent(byte[] content) {
		this.content = content;
	}

	public ImageSource getSource() {
		return source;
	}

	public void setSource(ImageSource source) {
		this.source = source;
	}
}
