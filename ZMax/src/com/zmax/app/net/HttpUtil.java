package com.zmax.app.net;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.zmax.app.utils.Log;

public class HttpUtil {
	
	/**
	 * 发送http请求
	 * 
	 * @param urlPath
	 *            请求路径
	 * @param requestType
	 *            请求类型
	 * @param request
	 *            请求参数，如果没有参数，则为null
	 * 
	 * @return
	 */
	public static String sendRequest(Context context, String urlPath, String requestType, String request) throws Exception {
		Log.d("HttpUtil", urlPath);
		URL url = null;
		HttpURLConnection conn = null;
		OutputStream os = null;
		InputStream is = null;
		String result = null;
		try {
			url = new URL(urlPath);
			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod(requestType);
			conn.setDoInput(true);
			conn.setDoOutput(true);
			conn.setConnectTimeout(15 * 1000);
			conn.setReadTimeout(15 * 1000);
			conn.setRequestProperty("Content-Type", "application/json");
			/*
			 * conn.addRequestProperty("TVersion", "13");
			 * conn.addRequestProperty("Phone-Model", PhoneUtil.getModel());
			 * conn.addRequestProperty("ScreenH",
			 * PhoneUtil.getScreenW(context));
			 * conn.addRequestProperty("ScreenW",
			 * PhoneUtil.getScreenH(context));
			 * conn.addRequestProperty("IMSI",
			 * PhoneUtil.getImsi(context));
			 * conn.addRequestProperty("Tuser-Agent",
			 * "BoutiqueApp/1.0 channel-2");
			 * conn.addRequestProperty("Android-release",
			 * PhoneUtil.getClientOsVersion());
			 * conn.addRequestProperty("Load",
			 * PhoneUtil.getNetworkType(context));
			 */
			conn.setRequestProperty("Connection", "keep-alive");
			Log.v("HttpUtil", "**" + "baseUrl:" + urlPath + ", requestMethod:" + requestType + ", request:" + request + "**");
			Log.v("HttpUtil", "**" + "RequestProperty:" + conn.getRequestProperties().toString() + "**");
			if (request != null && !"".equals(request)) {
				os = conn.getOutputStream();
				os.write(request.getBytes());
				os.flush();
			}
			Log.i("HttpUtil", "code=" + conn.getResponseCode());
			
			if (200 == conn.getResponseCode()) {
				is = conn.getInputStream();
				byte[] temp = readStream(is);
				result = new String(temp);
			}
			
		}
		catch (Exception e) {
			throw e;
		}
		finally {
			try {
				if (is != null) {
					is.close();
				}
				if (os != null) {
					os.close();
				}
				if (conn != null) {
					conn.disconnect();
				}
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	
	public static byte[] readStream(InputStream is) throws Exception {
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		byte[] buffer = new byte[2048];
		int len = 0;
		while ((len = is.read(buffer)) != -1) {
			os.write(buffer, 0, len);
		}
		is.close();
		return os.toByteArray();
	}
	
	public static interface HttpRequestErrorHandler {
		void handleError(int actionId, Exception e);
	}
	
	public static boolean isNetworkOk(Context context1) {
		Context context = context1;
		ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity == null) {
			return false;
		}
		else {
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null) {
				for (int i = 0; i < info.length; i++) {
					if (info[i].getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}
				}
			}
		}
		return false;
	}
}
