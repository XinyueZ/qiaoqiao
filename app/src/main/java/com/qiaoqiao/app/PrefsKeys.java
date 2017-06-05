package com.qiaoqiao.app;

public interface PrefsKeys {
	String KEY_GOOGLE_ID = "googleId";
	String KEY_GOOGLE_PHOTO_URL = "googlePhotoUrl";
	String KEY_GOOGLE_DISPLAY_NAME = "googleDisplayName";

	String DEFAULT_GOOGLE_ID = null;
	String DEFAULT_GOOGLE_PHOTO_URL = null;
	String DEFAULT_GOOGLE_DISPLAY_NAME = null;

	String KEY_CONFIDENCE_LABEL = "confidenceLabel";
	String KEY_CONFIDENCE_LOGO = "confidenceLogo";
	String KEY_CONFIDENCE_IMAGE = "confidenceImage";

	float DEFAULT_CONFIDENCE_LABEL = 0.9f;
	float DEFAULT_CONFIDENCE_LOGO = 0.9f;
	float DEFAULT_CONFIDENCE_IMAGE = 0.5f;

	String KEY_GEOSEARCH_RADIUS = "geosearchRadius";
	int DEFAULT_GEOSEARCH_RADIUS = 500L;
}
