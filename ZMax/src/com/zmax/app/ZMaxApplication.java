package com.zmax.app;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap.CompressFormat;
import android.util.Log;
import cn.sharesdk.framework.ShareSDK;

import com.baidu.mapapi.BMapManager;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.zmax.app.utils.Constant;
import com.zmax.app.utils.FileUtil;

public class ZMaxApplication extends Application {
	
	private static final String TAG = ZMaxApplication.class.getSimpleName();
	private static ZMaxApplication mInstance = null;
	public BMapManager mBMapManager = null;
	
	@Override
	public void onCreate() {
		super.onCreate();
		mInstance = this;
		Log.e("ZMaxApplication", "oncreate");
		init();
	}
	
	public static ZMaxApplication getInstance() {
		if (mInstance == null) {
			mInstance = new ZMaxApplication();
		}
		return mInstance;
	}
	
	public void init() {
		
		initImageLoader();
		// initEngineManager(this);
		ShareSDK.initSDK(this);// ShareSDK.stopSDK(this);?
		Thread.setDefaultUncaughtExceptionHandler(new CustomExceptionHandler());
		
	}
	
	/*
	 * baidu map
	 */
	public void initEngineManager(Context context) {
		if (mBMapManager == null) {
			mBMapManager = new BMapManager(context);
		}
		mBMapManager.init(Constant.MAP_SDK_KEY, null);
	}
	
	public static void initImageLoader() {
		ImageLoaderConfiguration config;
		DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory().cacheOnDisc().delayBeforeLoading(100)
				.displayer(new FadeInBitmapDisplayer(650)).showImageOnLoading(R.drawable.default_loading_img)
				.showImageForEmptyUri(R.drawable.default_loading_fail_img).showImageOnFail(R.drawable.default_loading_fail_img).build();
		config = new ImageLoaderConfiguration.Builder(mInstance).threadPriority(Thread.NORM_PRIORITY - 2)
				.denyCacheImageMultipleSizesInMemory().discCacheExtraOptions(480, 800, CompressFormat.JPEG, 90, null)
				.discCacheFileNameGenerator(new HashCodeFileNameGenerator()).tasksProcessingOrder(QueueProcessingType.LIFO)
				.discCache(new UnlimitedDiscCache(FileUtil.getSdcardDir())).discCacheSize(4 * 1024 * 1024).discCacheFileCount(100)
				.defaultDisplayImageOptions(options).build();
		ImageLoader.getInstance().init(config);
		com.nostra13.universalimageloader.utils.L.disableLogging();
	}
	
	public static void initImageLoader4Chat() {
		ImageLoaderConfiguration config;
		DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory().cacheOnDisc().build();
		config = new ImageLoaderConfiguration.Builder(mInstance).threadPriority(Thread.NORM_PRIORITY - 2)
				.denyCacheImageMultipleSizesInMemory().discCacheExtraOptions(480, 800, CompressFormat.JPEG, 90, null)
				.discCacheFileNameGenerator(new Md5FileNameGenerator()).tasksProcessingOrder(QueueProcessingType.LIFO)
				.discCache(new UnlimitedDiscCache(FileUtil.getSdcardDir4Chat())).discCacheSize(4 * 1024 * 1024).discCacheFileCount(100)
				.defaultDisplayImageOptions(options).build();
		ImageLoader.getInstance().init(config);
		com.nostra13.universalimageloader.utils.L.disableLogging();
	}
	
	/**
	 * 打印日志
	 */
	static class CustomExceptionHandler implements UncaughtExceptionHandler {
		
		private UncaughtExceptionHandler defaultUEH;
		
		public CustomExceptionHandler() {
			defaultUEH = Thread.getDefaultUncaughtExceptionHandler();
		}
		
		public void uncaughtException(Thread t, Throwable e) {
			final Writer result = new StringWriter();
			final PrintWriter printWriter = new PrintWriter(result);
			e.printStackTrace(printWriter);
			String stacktrace = result.toString();
			printWriter.close();
			com.zmax.app.utils.Log.e("ERROR:" + stacktrace);
			defaultUEH.uncaughtException(t, e);
		}
	}
}
