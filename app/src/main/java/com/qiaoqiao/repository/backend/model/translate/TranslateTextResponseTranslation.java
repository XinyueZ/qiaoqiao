package com.qiaoqiao.repository.backend.model.translate;

public final class TranslateTextResponseTranslation {
	private String detectedSourceLanguage;
	private String model;
	private String translatedText;


	public TranslateTextResponseTranslation(String detectedSourceLanguage, String model, String translatedText) {
		this.detectedSourceLanguage = detectedSourceLanguage;
		this.model = model;
		this.translatedText = translatedText;
	}


	public String getDetectedSourceLanguage() {
		return detectedSourceLanguage;
	}

	public void setDetectedSourceLanguage(String detectedSourceLanguage) {
		this.detectedSourceLanguage = detectedSourceLanguage;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getTranslatedText() {
		return translatedText;
	}

	public void setTranslatedText(String translatedText) {
		this.translatedText = translatedText;
	}
}
