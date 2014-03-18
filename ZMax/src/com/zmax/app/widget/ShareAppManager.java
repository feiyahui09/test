package com.zmax.app.widget;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.zmax.app.R;
import com.zmax.app.utils.FileUtils;

/**
 * Created by ben on 14-1-24. 分享的相关app管理 包括 1. 获取可以获取分享接口的app 2. 对应的类型分享设置
 */
public final class ShareAppManager {

	private static String LogTag = ShareAppManager.class.getCanonicalName();
	private final String[] packageNameList = { "com.android.mms",
			"com.alibaba.android.babylon", "com.tencent.mm",
			"com.tencent.mobileqq", "com.sina.weibo" };

	public static final List<Map<String, String>> sharePackageList = new ArrayList<Map<String, String>>() {
		{
			add(new HashMap<String, String>() {
				{
					put("name", "微信-朋友");
					put("package", "com.tencent.mm");
					put("launcher", "com.tencent.mm.ui.tools.ShareImgUI");
					put("icon", "logo_weixin.png");

				}
			});
			add(new HashMap<String, String>() {
				{
					put("name", "微信-朋友圈");
					put("package", "com.tencent.mm");
					put("launcher", "com.tencent.mm.ui.tools.ShareToTimeLineUI");
					put("icon", "logo_weixin.png");

				}
			});
			add(new HashMap<String, String>() {
				{
					put("name", "微博");
					put("package", "com.sina.weibo");
					put("launcher", "com.sina.weibo.EditActivity");
					put("icon", "logo_weibo.png");
				}
			});
			add(new HashMap<String, String>() {
				{
					put("name", "QQ");
					put("package", "com.tencent.mobileqq");
					put("launcher",
							"com.tencent.mobileqq.activity.JumpActivity");
					put("icon", "logo_qq.png");
				}
			});

			add(new HashMap<String, String>() {
				{
					put("name", "来往-聊天");
					put("package", "com.alibaba.android.babylon");
					put("launcher",
							"com.alibaba.android.babylon.biz.im.activity.RecentIMListActivity");
					put("icon", "logo_laiwang.png");
				}
			});

			add(new HashMap<String, String>() {
				{
					put("name", "来往-动态");
					put("package", "com.alibaba.android.babylon");
					put("launcher",
							"com.alibaba.android.babylon.biz.home.activity.WriteFeedActivity");
					put("icon", "logo_laiwang.png");
				}
			});

			add(new HashMap<String, String>() {
				{
					put("name", "短信");
					put("package", "com.android.mms");
					put("launcher", "com.android.mms.ui.ComposeMessageActivity");

				}
			});
		}
	};

	/**
	 * 获取特定分享的app
	 * 
	 * @param context
	 * @return
	 */
	public List<ResolveInfo> getShareApps(Context context) {
		List<ResolveInfo> mApps = null;
		Intent intent = new Intent(Intent.ACTION_SEND, null);
		intent.addCategory(Intent.CATEGORY_DEFAULT);
		intent.setType("image/*");
		// intent.setType("text/plain");
		PackageManager pManager = context.getPackageManager();
		if (null != pManager) {
			mApps = pManager.queryIntentActivities(intent,
					PackageManager.MATCH_DEFAULT_ONLY);
		}
		List<ResolveInfo> removeList = new ArrayList<ResolveInfo>();
		// Log.d(LogTag, Arrays.toString(packageNameList));
		for (ResolveInfo info : mApps) {
			if (info.activityInfo.name.contains("qfile")) {
				continue;
			}

			for (String request : packageNameList) {
				if (request.equalsIgnoreCase(info.activityInfo.packageName)) {
					removeList.add(info);
				}
			}

		}
		mApps.retainAll(removeList);
		Log.d(LogTag, mApps.toString());
		return mApps;
	}

	/**
	 * 检查对应的package是否已经安装
	 * 
	 * @param context
	 * @param packageName
	 */
	private static boolean checkPackage(Context context, String packageName) {
		PackageManager packageManager = context.getPackageManager();
		try {
			PackageInfo pInfo = packageManager.getPackageInfo(packageName,
					PackageManager.GET_ACTIVITIES);
			// 判断是否获取到了对应的包名信息
			if (pInfo != null) {
				return true;
			}
		} catch (PackageManager.NameNotFoundException e) {
			return false;
		}
		return false;
	}

	/**
	 * 是否安装了市场app
	 * 
	 * @param context
	 * @param intent
	 * @return
	 */
	private static boolean isInstalledMarket(Context context, Intent intent) {
		List<ResolveInfo> marketApps = context.getPackageManager()
				.queryIntentActivities(intent,
						PackageManager.GET_INTENT_FILTERS);
		if (marketApps != null && marketApps.size() > 0) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 分享对应的app.并设置对应的属性,
	 */
	public static void shareTo(Context context, String packageName,
			String launcherClassName, String content) {
		try {
			if (checkPackage(context, packageName)) {
				Intent share = new Intent(Intent.ACTION_SEND);
				share.setPackage(packageName);
				// share.setClassName(packageName, launcherClassName);
				Log.d(LogTag, String.format("shareTo launcher > %s",
						launcherClassName));
				// 默认是文字形式分享
				share.setType("text/plain");

				share.putExtra(Intent.EXTRA_TEXT, content);
				if (Environment.MEDIA_MOUNTED.equals(Environment
						.getExternalStorageState())) {
					try {

						share.setType("image/png");
						File tempFile = new File(FileUtils.getSDRoot(),
								"share.jpeg");
						if (!tempFile.exists()) {
							tempFile.createNewFile();
							FileOutputStream fOut = new FileOutputStream(
									tempFile);
							Bitmap bm = BitmapFactory.decodeResource(
									context.getResources(),
									R.drawable.ic_launcher);
							bm.compress(Bitmap.CompressFormat.PNG, 100, fOut);
							fOut.flush();
							fOut.close();
						}
						share.putExtra(Intent.EXTRA_STREAM,
								Uri.fromFile(tempFile));
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				// 短信
				if (packageName.equalsIgnoreCase("com.android.mms")) {
					share.putExtra("sms_body", content);
					share.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					context.startActivity(share);
				}

				if (packageName.equalsIgnoreCase("com.sina.weibo")
						|| packageName
								.equalsIgnoreCase("com.alibaba.android.babylon")
						|| packageName.equalsIgnoreCase("com.tencent.mobileqq")) {
					share.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

					context.startActivity(share);
				}
				// 微信朋友圈
				if (launcherClassName.contains("ShareToTimeLineUI")) {
					new ShareWeiXin(context).shareToFriendTimeline(content);
				}
				// 微信朋友
				if (launcherClassName.contains("ShareImgUI")) {
					new ShareWeiXin(context).shareToFriend(content);
				}

			} else {// 找不到对应的app来进行分享,只好让用户下载了

				Intent gotoMarket = new Intent();

				gotoMarket.setAction(Intent.ACTION_VIEW);
				gotoMarket.setData(Uri.parse(String.format(
						"market://search?q=pname:%s", packageName)));

				if (isInstalledMarket(context, gotoMarket)) {
					context.startActivity(gotoMarket);
				} else {
					context.startActivity(new Intent(Intent.ACTION_VIEW).setData(Uri.parse(String
							.format("http://market.android.com/search?q=pname:%s",
									packageName))));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.e("ShareAppManager", e.getMessage());
		}
	}
}
