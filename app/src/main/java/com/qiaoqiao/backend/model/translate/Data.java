package com.qiaoqiao.backend.model.translate;


public final class Data {
	private TranslateTextResponseTranslation[] translations;

	public Data(TranslateTextResponseTranslation[] translations) {
		this.translations = translations;
	}

	public TranslateTextResponseTranslation[] getTranslations() {
		return translations;
	}

	public void setTranslations(TranslateTextResponseTranslation[] translations) {
		this.translations = translations;
	}
}
