package com.qiaoqiao.keymanager;


import android.content.Context;
import android.support.annotation.NonNull;

import java.io.IOException;
import java.util.Properties;

import dagger.Module;
import dagger.Provides;

@Module
public class KeyManagerModule {

	@Provides
	Key provideKey(@NonNull Context cxt) {
		String key = null;
		Properties prop = new Properties();
		try {
			prop.load(cxt.getClassLoader()
			             .getResourceAsStream("key.properties"));
			key = prop.getProperty("apikey");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new Key(key);
	}
}
