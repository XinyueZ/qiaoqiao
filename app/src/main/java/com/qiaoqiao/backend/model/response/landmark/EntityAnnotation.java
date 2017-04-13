package com.qiaoqiao.backend.model.response.landmark;


import com.google.gson.annotations.SerializedName;

public final class EntityAnnotation {
	@SerializedName("mid") private String mid;
	@SerializedName("locale") private String locale;
	@SerializedName("description") private String description;
	@SerializedName("score") private double score;
	@SerializedName("confidence") private double confidence;
	@SerializedName("locations") private LocationInfo[] locations;
	@SerializedName("properties") private Property[] properties;

	public EntityAnnotation(String mid, String locale, String description, double score, double confidence, LocationInfo[] locations, Property[] properties) {
		this.mid = mid;
		this.locale = locale;
		this.description = description;
		this.score = score;
		this.confidence = confidence;
		this.locations = locations;
		this.properties = properties;
	}


	public String getMid() {
		return mid;
	}

	public void setMid(String mid) {
		this.mid = mid;
	}

	public String getLocale() {
		return locale;
	}

	public void setLocale(String locale) {
		this.locale = locale;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public double getScore() {
		return score;
	}

	public void setScore(double score) {
		this.score = score;
	}

	public double getConfidence() {
		return confidence;
	}

	public void setConfidence(double confidence) {
		this.confidence = confidence;
	}

	public LocationInfo[] getLocations() {
		return locations;
	}

	public void setLocations(LocationInfo[] locations) {
		this.locations = locations;
	}

	public Property[] getProperties() {
		return properties;
	}

	public void setProperties(Property[] properties) {
		this.properties = properties;
	}
}
