package com.zmax.app.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.onekeyshare.OnekeyShare;

import com.zmax.app.R;

public class ShareUtils {

	private String TEST_IMAGE = "";

	// 使用快捷分享完成分享（请务必仔细阅读位于SDK解压目录下Docs文件夹中OnekeyShare类的JavaDoc）
	/**
	 * ShareSDK集成方法有两种</br>
	 * 1、第一种是引用方式，例如引用onekeyshare项目，onekeyshare项目再引用mainlibs库</br>
	 * 2、第二种是把onekeyshare和mainlibs集成到项目中，本例子就是用第二种方式</br> 请看“ShareSDK
	 * 使用说明文档”，SDK下载目录中 </br> 或者看网络集成文档
	 * http://wiki.sharesdk.cn/Android_%E5%BF%AB
	 * %E9%80%9F%E9%9B%86%E6%88%90%E6%8C%87%E5%8D%97
	 * 3、混淆时，把sample或者本例子的混淆代码copy过去，在proguard-project.txt文件中
	 * 
	 * 
	 * 平台配置信息有三种方式： 1、在我们后台配置各个微博平台的key
	 * 2、在代码中配置各个微博平台的key，http://sharesdk.cn/androidDoc
	 * /cn/sharesdk/framework/ShareSDK.html
	 * 3、在配置文件中配置，本例子里面的assets/ShareSDK.conf,
	 */
	public void showShare(Context context, boolean silent, String platform) {
		initPic(context);
		try {
			Thread.sleep(122);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		final OnekeyShare oks = new OnekeyShare();
		oks.setNotification(R.drawable.ic_launcher,
				context.getString(R.string.app_name));
		oks.setAddress("12345678901");
		oks.setTitle(context.getString(R.string.share_title));
		oks.setTitleUrl("http://sharesdk.cn");
		oks.setText(context.getString(R.string.share_content));
		  oks.setImagePath( TEST_IMAGE);
		// oks.setImageUrl(MainActivity.TEST_IMAGE_URL);
		oks.setUrl("http://www.sharesdk.cn");
		// oks.setFilePath(MainActivity.TEST_IMAGE);
		oks.setComment(context.getString(R.string.share));
		oks.setSite(context.getString(R.string.app_name));
		oks.setSiteUrl("http://sharesdk.cn");
		oks.setVenueName("ShareSDK");
		oks.setVenueDescription("This is a beautiful place!");
		oks.setLatitude(23.056081f);
		oks.setLongitude(113.385708f);
		oks.setSilent(silent);
		if (platform != null) {
			oks.setPlatform(platform);
		}

		// 去除注释，可令编辑页面显示为Dialog模式
		// oks.setDialogMode();

		// 去除注释，在自动授权时可以禁用SSO方式
		// oks.disableSSOWhenAuthorize();

		// 去除注释，则快捷分享的操作结果将通过OneKeyShareCallback回调
		oks.setCallback(new OneKeyShareCallback());
		// oks.setShareContentCustomizeCallback(new
		// ShareContentCustomizeDemo());

		// 去除注释，演示在九宫格设置自定义的图标
		// Bitmap logo = BitmapFactory.decodeResource(menu.getResources(),
		// R.drawable.ic_launcher);
		// String label = menu.getResources().getString(R.string.app_name);
		// OnClickListener listener = new OnClickListener() {
		// public void onClick(View v) {
		// String text = "Customer Logo -- ShareSDK " +
		// ShareSDK.getSDKVersionName();
		// Toast.makeText(menu.context, text, Toast.LENGTH_SHORT).show();
		// oks.finish();
		// }
		// };
		// oks.setCustomerLogo(logo, label, listener);

		// 去除注释，则快捷分享九宫格中将隐藏新浪微博和腾讯微博
		// oks.addHiddenPlatform(SinaWeibo.NAME);
		// oks.addHiddenPlatform(TencentWeibo.NAME);

		oks.show(context);
	}

	/**
	 * OneKeyShareCallback是快捷分享功能的一个“外部回调”示例。通过
	 * {@link OnekeyShare#setCallback(PlatformActionListener)}将
	 * 本类的示例传递进快捷分享，分享操作结束后，快捷分享会将分享结果 回调到本类中来做自定义处理。
	 */
	class OneKeyShareCallback implements PlatformActionListener {

		public void onComplete(Platform plat, int action,
				HashMap<String, Object> res) {
			System.out.println(res.toString());
			// 在这里添加分享成功的处理代码
		}

		public void onError(Platform plat, int action, Throwable t) {
			t.printStackTrace();
			// 在这里添加分享失败的处理代码
		}

		public void onCancel(Platform plat, int action) {
			// 在这里添加取消分享的处理代码
		}

	}

	public void initPic(final Context context ) {
		new Thread() {
			public void run() {
				initImagePath(context);
			}
		}.start();
	}

	private void initImagePath(Context context) {
		try {
			String cachePath = cn.sharesdk.framework.utils.R.getCachePath(context,
					null);
			TEST_IMAGE = cachePath + "share_file.jpg";
			File file = new File(TEST_IMAGE);
			if (!file.exists()) {
				file.createNewFile();
				Bitmap pic = BitmapFactory.decodeResource(context.getResources(),
						R.drawable.icon);
				FileOutputStream fos = new FileOutputStream(file);
				pic.compress(CompressFormat.JPEG, 100, fos);
				fos.flush();
				fos.close();
			}
		} catch (Throwable t) {
			t.printStackTrace();
			TEST_IMAGE = null;
		}
	}
}