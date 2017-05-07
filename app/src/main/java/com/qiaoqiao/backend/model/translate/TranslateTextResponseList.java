package com.qiaoqiao.backend.model.translate;


public final class TranslateTextResponseList {
	private TranslateTextResponseTranslation[] translations;

	public TranslateTextResponseList(TranslateTextResponseTranslation[] translations) {
		this.translations = translations;
	}


	public TranslateTextResponseTranslation[] getTranslations() {
		return translations;
	}

	public void setTranslations(TranslateTextResponseTranslation[] translations) {
		this.translations = translations;
	}
}
