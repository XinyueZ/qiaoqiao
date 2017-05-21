package com.qiaoqiao.customtabs;


import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.customtabs.CustomTabsClient;
import android.support.customtabs.CustomTabsIntent;
import android.support.customtabs.CustomTabsServiceConnection;
import android.support.customtabs.CustomTabsSession;

import java.util.List;

public final class CustomTabsActivityHelper implements ServiceConnectionCallback {
	private CustomTabsSession mCustomTabsSession;
	private CustomTabsClient mClient;
	private CustomTabsServiceConnection mConnection;
	private ConnectionCallback mConnectionCallback;

	public static void openCustomTab(Activity activity, CustomTabsIntent customTabsIntent, String title, Uri uri, CustomTabFallback fallback) {
		String packageName = CustomTabsHelper.getPackageNameToUse(activity);

		//If we cant find a package name, it means theres no browser that supports
		//Chrome Custom Tabs installed. So, we fallback to the browser
		if (packageName == null) {
			if (fallback != null) {
				fallback.openUrl(activity, title, uri);
			}
		} else {
			customTabsIntent.intent.setPackage(packageName);
			customTabsIntent.launchUrl(activity, uri);
		}
	}

	interface CustomTabFallback {
		void openUrl(Activity activity, String title, @NonNull Uri uri);
	}


	/**
	 * Unbinds the Activity from the Custom Tabs Service.
	 *
	 * @param cxt the {@link Context} that is connected to the service.
	 */
	public void unbindCustomTabsService(Context cxt) {
		if (mConnection == null) {
			return;
		}
		cxt.unbindService(mConnection);
		mClient = null;
		mCustomTabsSession = null;
		mConnection = null;
	}

	/**
	 * Binds the Activity to the Custom Tabs Service.
	 *
	 * @param cxt the {@link Context} that is connected to the service.
	 * @return {@link CustomTabsActivityHelper}
	 */
	public CustomTabsActivityHelper bindCustomTabsService(Context cxt) {
		if (mClient != null) {
			return this;
		}

		String packageName = CustomTabsHelper.getPackageNameToUse(cxt);
		if (packageName == null) {
			return this;
		}

		mConnection = new ServiceConnection(this);
		CustomTabsClient.bindCustomTabsService(cxt, packageName, mConnection);
		return this;
	}


	/**
	 * @return true if call to mayLaunchUrl was accepted.
	 * @see {@link CustomTabsSession#mayLaunchUrl(Uri, Bundle, List)}.
	 */
	public boolean mayLaunchUrl(Uri uri, Bundle extras, List<Bundle> otherLikelyBundles) {
		if (mClient == null) {
			return false;
		}

		CustomTabsSession session = getSession();
		return session != null && session.mayLaunchUrl(uri, extras, otherLikelyBundles);

	}


	/**
	 * A Callback for when the service is connected or disconnected. Use those callbacks to handle UI changes when the
	 * service is connected or disconnected.
	 */
	public interface ConnectionCallback {
		/**
		 * Called when the service is connected.
		 */
		void onCustomTabsConnected();

		/**
		 * Called when the service is disconnected.
		 */
		void onCustomTabsDisconnected();
	}

	/**
	 * Register a Callback to be called when connected or disconnected from the Custom Tabs Service.
	 *
	 * @param connectionCallback
	 */
	public void setConnectionCallback(ConnectionCallback connectionCallback) {
		this.mConnectionCallback = connectionCallback;
	}

	/**
	 * Creates or retrieves an exiting CustomTabsSession.
	 *
	 * @return a CustomTabsSession.
	 */
	private CustomTabsSession getSession() {
		if (mClient == null) {
			mCustomTabsSession = null;
		} else if (mCustomTabsSession == null) {
			mCustomTabsSession = mClient.newSession(null);
		}
		return mCustomTabsSession;
	}

	@Override
	public void onServiceConnected(CustomTabsClient client) {
		mClient = client;
		mClient.warmup(0L);
		if (mConnectionCallback != null) {
			mConnectionCallback.onCustomTabsConnected();
		}
	}

	@Override
	public void onServiceDisconnected() {
		mClient = null;
		mCustomTabsSession = null;
		if (mConnectionCallback != null) {
			mConnectionCallback.onCustomTabsDisconnected();
		}
	}
}
