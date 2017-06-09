package com.qiaoqiao.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;

/**
 * Utils for network.
 */
public final class NetworkUtils {
	public static final byte CONNECTION_OFFLINE = 1;
	public static final byte CONNECTION_WIFI = 2;
	public static final byte CONNECTION_ROAMING = 3;
	public static final byte CONNECTION_SLOW = 4;
	public static final byte CONNECTION_FAST = 5;


	private NetworkUtils() {
	}


	/**
	 * Check if the device is connected to the internet (mobile network or WIFI).
	 */
	public static boolean isOnline(Context _context) {
		boolean online = false;

		TelephonyManager tmanager = (TelephonyManager) _context.getSystemService(Context.TELEPHONY_SERVICE);
		if (tmanager != null) {
			if (tmanager.getDataState() == TelephonyManager.DATA_CONNECTED) {
				// Mobile network
				online = true;
			} else {
				// WIFI
				ConnectivityManager cmanager = (ConnectivityManager) _context.getSystemService(Context.CONNECTIVITY_SERVICE);
				if (cmanager != null) {
					NetworkInfo info = cmanager.getActiveNetworkInfo();
					if (info != null) {
						online = info.isConnected();
					}
				}
			}
		}

		return online;
	}


	/**
	 * Evaluate the current network connection and return the corresponding type, e.g. CONNECTION_WIFI.
	 */
	public static byte getCurrentNetworkType(Context _context) {
		NetworkInfo netInfo = ((ConnectivityManager) _context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();

		if (netInfo == null) {
			return CONNECTION_OFFLINE;
		}

		if (netInfo.getType() == ConnectivityManager.TYPE_WIFI) {
			return CONNECTION_WIFI;
		}

		if (netInfo.isRoaming()) {
			return CONNECTION_ROAMING;
		}

		if (!(netInfo.getType() == ConnectivityManager.TYPE_MOBILE && (netInfo.getSubtype() == TelephonyManager.NETWORK_TYPE_UMTS || netInfo.getSubtype() == TelephonyManager.NETWORK_TYPE_HSDPA ||
				netInfo.getSubtype() == TelephonyManager.NETWORK_TYPE_HSUPA || netInfo.getSubtype() == TelephonyManager.NETWORK_TYPE_HSPA || netInfo.getSubtype() == 13 // NETWORK_TYPE_LTE
				|| netInfo.getSubtype() == 15))) // NETWORK_TYPE_HSPAP
		{

			return CONNECTION_SLOW;
		}

		return CONNECTION_FAST;
	}


	/**
	 * Test for whether Airplane-Mode has been on or off.
	 *
	 * @param context A context object.
	 * @return True if airplane on, false if off.
	 */
	@SuppressWarnings("deprecation")
	@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
	public static boolean isAirplaneModeOn(Context context) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
			return Settings.System.getInt(context.getContentResolver(), Settings.Global.AIRPLANE_MODE_ON, 0) != 0;
		} else {
			return Settings.System.getInt(context.getContentResolver(), Settings.System.AIRPLANE_MODE_ON, 0) != 0;
		}
	}
}
