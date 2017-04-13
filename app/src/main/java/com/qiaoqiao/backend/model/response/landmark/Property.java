package com.qiaoqiao.backend.model.response.landmark;


import com.google.gson.annotations.SerializedName;

public final class Property {
	@SerializedName("name") private String name;
	@SerializedName("value") private String value;
	@SerializedName("uint64Value") private String uint64Value;

	public Property(String name, String value, String uint64Value) {
		this.name = name;
		this.value = value;
		this.uint64Value = uint64Value;
	}


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getUint64Value() {
		return uint64Value;
	}

	public void setUint64Value(String uint64Value) {
		this.uint64Value = uint64Value;
	}
}
