package com.qiaoqiao.backend.model.translate;


public final class Data {
	private TranslateTextResponseList translations;

	public Data(TranslateTextResponseList translations) {
		this.translations = translations;
	}


	public TranslateTextResponseList getTranslations() {
		return translations;
	}

	public void setTranslations(TranslateTextResponseList translations) {
		this.translations = translations;
	}
}
