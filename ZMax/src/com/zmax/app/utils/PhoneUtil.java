package com.zmax.app.utils;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.WindowManager;

public class PhoneUtil {
	private static String imsi;
	
	/**
	 * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
	 */
	public static int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}
	
	/**
	 * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
	 */
	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}
	
	public static String getIMSI(final Context context) {
		TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		return telephonyManager.getSubscriberId() != null ? telephonyManager.getSubscriberId() : "";
	}
	
	public static String getEsn(final Context context) {
		TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		String imei = tm.getDeviceId();
		return imei;
	}
	
	public static String getModel() {
		String model = Build.MODEL;
		
		if ("sdk".equals(model)) {
			model = "XT800";
		}
		return model;
	}
	
	public static String getBrand() {
		String brand = Build.BRAND == null ? "" : Build.BRAND;
		return brand;
	}
	
	public static String getClientOsVersion() {
		String clientOsVersion = "android" + Build.VERSION.RELEASE;
		// Log.d(TAG,"dddd" + Build.VERSION_CODES.CUR_DEVELOPMENT);
		return clientOsVersion;
	}
	
	public static String getPixel(Context ctx) {
		WindowManager wm = (WindowManager) ctx.getSystemService(Context.WINDOW_SERVICE);
		String pixel = wm.getDefaultDisplay().getHeight() + "*" + wm.getDefaultDisplay().getWidth();
		return pixel;
	}
	
	/**
	 * retreive current active network type mobile/wifi
	 * 
	 * @param context
	 * @return
	 */
	public static String getNetworkType(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo ni = cm.getActiveNetworkInfo();
		if (ni != null) {
			int type = ni.getType();
			switch (type) {
				case ConnectivityManager.TYPE_MOBILE:
					return "3G";
				case ConnectivityManager.TYPE_WIFI:
					return "WIFI";
			}
		}
		
		return "";
	}
	
	public static String getNetConectionType(Context context) {
		ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
		String netConectionType = "";
		
		if (activeNetInfo != null && activeNetInfo.isConnected()) {
			if (activeNetInfo.getType() == ConnectivityManager.TYPE_WIFI) {
				netConectionType = "wifi";
				
			}
			else if (activeNetInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
				netConectionType = "2G/3G";
			}
		}
		return netConectionType;
	}
	
	/**
	 * 根据包名获取应用版本号
	 * 
	 * @param context
	 * @param pkgName
	 * @return versionCode of the pkgName
	 * 
	 * @author LiuJQ
	 * @time 2013-11-5 17:26:18
	 */
	public static String getVersionCode(Context context, String pkgName) {
		String versionCode = "";
		if (TextUtils.isEmpty(pkgName)) {
			return versionCode;
		}
		PackageManager manager = context.getPackageManager();
		
		PackageInfo info;
		try {
			info = manager.getPackageInfo(pkgName, 0);
			versionCode = String.valueOf(info.versionCode);
			
		}
		catch (NameNotFoundException e) {
			
		}
		return versionCode;
	}
	
	/**
	 * 根据包名获取应用版本名
	 * 
	 * @param context
	 * @param pkgName
	 * @return versionName of the pkgName
	 * 
	 * @author LiuJQ
	 * @time 2013-11-5 17:26:18
	 */
	public static String getVersionName(Context context, String pkgName) {
		String versionName = "";
		if (TextUtils.isEmpty(pkgName)) {
			return versionName;
		}
		PackageManager manager = context.getPackageManager();
		
		PackageInfo info;
		try {
			info = manager.getPackageInfo(pkgName, 0);
			versionName = info.versionName;
			
		}
		catch (NameNotFoundException e) {
			
		}
		return versionName;
	}
	
	public static String getVersionName(Context ctx) {
		String versionName = "";
		PackageManager manager = ctx.getPackageManager();
		
		PackageInfo info;
		try {
			info = manager.getPackageInfo(ctx.getPackageName(), 0);
			versionName = info.versionName;
			
		}
		catch (NameNotFoundException e) {
			
		}
		return versionName;
	}
	
	/**
	 * 
	 * @param ctx
	 * @return
	 */
	public static boolean isNetworkOk(Context ctx) {
		ConnectivityManager connectivity = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetInfo = connectivity.getActiveNetworkInfo();
		if (activeNetInfo != null && activeNetInfo.isConnected()) {
			return true;
		}
		return false;
	}
	
	public static class APNNET {
		public static String CTWAP = "ctwap";
		public static String CTNET = "ctnet";
		public static String NOMATCH = "nomatch";
	}
	
	public static String getScreenW(Context ctx) {
		WindowManager wm = (WindowManager) ctx.getSystemService(Context.WINDOW_SERVICE);
		return "" + wm.getDefaultDisplay().getWidth();
	}
	
	public static String getScreenH(Context ctx) {
		WindowManager wm = (WindowManager) ctx.getSystemService(Context.WINDOW_SERVICE);
		return "" + wm.getDefaultDisplay().getHeight();
	}
	
	public static String GetHostIp() {
		try {
			for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
				NetworkInterface intf = en.nextElement();
				for (Enumeration<InetAddress> ipAddr = intf.getInetAddresses(); ipAddr.hasMoreElements();) {
					InetAddress inetAddress = ipAddr.nextElement();
					if (!inetAddress.isLoopbackAddress()) {
						return inetAddress.getHostAddress();
					}
				}
			}
		}
		catch (Exception e) {
		}
		return null;
	}
}
