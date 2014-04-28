package com.zmax.app.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import org.codehaus.jackson.map.ser.ToStringSerializer;

import com.zmax.app.R;
import com.zmax.app.ZMaxApplication;
import com.zmax.app.net.NetWorkHelper;
import com.zmax.app.task.GetActListTask;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Utility {
	
	public static void goDialPhone(Context context, String phoNum) {
		Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoNum));
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(intent);
		
	}
	
	public static boolean isETNull(EditText editText) {
		if (TextUtils.isEmpty(editText.getText().toString().trim())) return true;
		return false;
	}
	
	public static String getDate() {
		return new SimpleDateFormat("HH:mm:ss").format(new Date());
	}
	
	public static int getRandom(int range) {
		Random random = new Random(System.currentTimeMillis());
		return random.nextInt(range);
	}
	
	public static void toastFailedResult(Context context) {
		Toast.makeText(context, "获取信息失败，请稍后再试！", 500).show();
	}
	
	public static void toastResult(Context context, String msg) {
		Toast.makeText(context, msg, 500).show();
	}
	
	public static void toastNetworkFailed(Context context) {
		Toast.makeText(context, context.getResources().getString(R.string.httpProblem), 300).show();
	}
	
	public static void toastNoMoreResult(Context context) {
		Toast.makeText(context, "没有更多了！", 500).show();
	}
	
	/**
	 * 
	 * 获取webviewClient对象
	 * 
	 * @return
	 */
	public static WebViewClient getWebViewClient() {
		return new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				// openBrowser(view.getContext(), url);
				view.loadUrl(url);
				return true;
			}
		};
	}
	
	/**
	 * 打开浏览器
	 * 
	 * @param context
	 * @param url
	 */
	public static void openBrowser(Context context, String url) {
		try {
			Uri uri = Uri.parse(url);
			Intent it = new Intent(Intent.ACTION_VIEW, uri);
			context.startActivity(it);
		}
		catch (Exception e) {
			e.printStackTrace();
			Toast.makeText(context, "无法浏览此网页", 500).show();
		}
	}
	
}
