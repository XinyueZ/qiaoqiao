package com.qiaoqiao.bus;

import android.net.Uri;
import android.support.annotation.NonNull;

public final class WebLinkInputEvent {
	private final Uri mUri;

	public WebLinkInputEvent(@NonNull  Uri uri) {
		mUri = uri;
	}

	public @NonNull  Uri getUri() {
		return mUri;
	}
}
