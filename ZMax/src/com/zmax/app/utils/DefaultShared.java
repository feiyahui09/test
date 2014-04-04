package com.zmax.app.utils;

import java.util.Map;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.zmax.app.ZMaxApplication;

/**
 * 
 * SharedPreferences支持数据类型存储 位置 packagename.xml
 * 
 */
public final class DefaultShared {
	
	private static final String DEFAULT_PREF = "zmax_default";
	
	public static final String EXTRA_IMSI = "extra_imsi";
	public static final String EXTRA_PROTOTAL = "extra_prototal";
	public static final String EXTRA_VERSION = "extra_version";
	public static final String EXTRA_ISFIRSTADD = "extra_isfirstadd";
	public static final String EXTRA_ISSHOW_GESTUREHINT = "extra_isshow_gesturehint";
	
	// private static final SharedPreferences spf = PreferenceManager
	// .getDefaultSharedPreferences(BoutiqueApplication.application
	// .getBaseContext());
	private static SharedPreferences spf = null;
	
	static {
		if (ZMaxApplication.getInstance() != null) {
			spf = ZMaxApplication.getInstance().getSharedPreferences(DEFAULT_PREF, Context.MODE_PRIVATE);
		}
	}
	
	public static void putBoolean(String key, boolean valeu) {
		if (spf != null) {
			spf.edit().putBoolean(key, valeu).commit();
		}
	}
	
	public static void putFloat(String key, float valeu) {
		if (spf != null) {
			spf.edit().putFloat(key, valeu).commit();
		}
	}
	
	public static void putInt(String key, int valeu) {
		if (spf != null) {
			spf.edit().putInt(key, valeu).commit();
		}
	}
	
	public static void putLong(String key, long valeu) {
		if (spf != null) {
			spf.edit().putLong(key, valeu).commit();
		}
	}
	
	public static void putString(String key, String valeu) {
		if (spf != null) {
			spf.edit().putString(key, valeu).commit();
		}
	}
	
	/**
	 * SharedPreferences支持数据类型
	 * 
	 * @param params
	 */
	public static void putMap(Map<String, ?> params) {
		if (params == null || params.size() == 0) return;
		
		if (spf != null) {
			Editor edit = spf.edit();
			for (Map.Entry<String, ?> entry : params.entrySet()) {
				if (entry.getValue() instanceof Boolean) {
					edit.putBoolean(entry.getKey(), (Boolean) entry.getValue());
				}
				else if (entry.getValue() instanceof Float) {
					edit.putFloat(entry.getKey(), (Float) entry.getValue());
				}
				else if (entry.getValue() instanceof Integer) {
					edit.putInt(entry.getKey(), (Integer) entry.getValue());
				}
				else if (entry.getValue() instanceof Long) {
					edit.putLong(entry.getKey(), (Long) entry.getValue());
				}
				else if (entry.getValue() instanceof String) {
					edit.putString(entry.getKey(), (String) entry.getValue());
				}
			}
			edit.commit();
		}
	}
	
	public static boolean getBoolean(String key, boolean defValue) {
		return spf != null ? spf.getBoolean(key, defValue) : defValue;
	}
	
	public static float getFloat(String key, float defValue) {
		
		return spf != null ? spf.getFloat(key, defValue) : defValue;
	}
	
	public static int getInt(String key, int defValue) {
		return spf != null ? spf.getInt(key, defValue) : defValue;
	}
	
	public static long getLong(String key, long defValue) {
		return spf != null ? spf.getLong(key, defValue) : defValue;
	}
	
	public static String getString(String key, String defValue) {
		return spf != null ? spf.getString(key, defValue) : defValue;
	}
	
	public static boolean isContainKey(String key) {
		return spf != null ? spf.contains(key) : false;
	}
	
}