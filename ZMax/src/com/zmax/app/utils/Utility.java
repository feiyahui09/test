package com.zmax.app.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import org.codehaus.jackson.map.ser.ToStringSerializer;

import com.zmax.app.R;
import com.zmax.app.ZMaxApplication;
import com.zmax.app.model.Login;
import com.zmax.app.net.NetWorkHelper;
import com.zmax.app.task.GetActListTask;

import eu.inmite.android.lib.dialogs.SimpleDialogFragment;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
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
		showToast(context, "获取信息失败，请稍后再试！");
	}
	
	public static void toastUnkownResult(Context context) {
		showToast(context, context.getString(R.string.unkownError));
	}
	
	public static void toastResult(Context context, String msg) {
		showToast(context, msg);
	}
	
	public static void toastNetworkFailed(Context context) {
		showToast(context, context.getResources().getString(R.string.httpProblem));
	}
	
	public static void toastNoMoreResult(Context context) {
		showToast(context, "没有更多了！");
	}
	
	private static Toast mToast;
	
	public static void showToast(Context context, String text) {
		if (TextUtils.isEmpty(text)) return;
		if (mToast == null) {
			mToast = Toast.makeText(context, text + "", Toast.LENGTH_SHORT);
		}
		else {
			mToast.setText(text);
			mToast.setDuration(Toast.LENGTH_SHORT);
		}
		mToast.show();
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
	
	public static void showTokenErrorDialog(FragmentActivity fragmentActivity, String msg) {
		SimpleDialogFragment.createBuilder(fragmentActivity, fragmentActivity.getSupportFragmentManager()).setPositiveButtonText("确定")
				.setTitle("提示").setMessage("Token验证失败，请重新登录！").setRequestCode(Constant.DialogCode.TYPE_TOKEN_ERROR).setCancelable(false)
				.show();
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
	
	public static int getChatThumbImgSize(Context context) {
		
		int sw = PhoneUtil.getScreenW(context);
		// if (sw > 1000) {
		sw = 1000;
		// }
		// else if (sw > 700) {
		// sw = 700;
		// }
		// else {
		// sw = 440;
		// }
		
		Log.i(" ChatThumbImgSize:" + sw);
		return sw;
	}
}
