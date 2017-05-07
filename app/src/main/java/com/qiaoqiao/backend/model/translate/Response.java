package com.qiaoqiao.backend.model.translate;


public final class Response {
	private Data data;


	public Response(Data data) {
		this.data = data;
	}


	public Data getData() {
		return data;
	}

	public void setData(Data data) {
		this.data = data;
	}
}
