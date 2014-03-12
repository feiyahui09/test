package com.zmax.app;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap.CompressFormat;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.zmax.app.utils.FileUtils;
import com.zmax.app.utils.Log;

public class ZMaxApplication extends Application {

	private static final String TAG = ZMaxApplication.class.getSimpleName();
	public static Context context;

	public static boolean initFlag = false;

	@Override
	public void onCreate() {
		super.onCreate();
		init(this);
		Log.d("TAG", "onCreate");

	}

	public static void init(Context mContext) {
		context = mContext;
		initFlag = true;

		// AdvertTickUpdate.register(mContext);
		initImageLoader();
		Thread.setDefaultUncaughtExceptionHandler(new CustomExceptionHandler());

	}

	private static void initImageLoader() {
		ImageLoaderConfiguration config;
		DisplayImageOptions options = new DisplayImageOptions.Builder()
				.cacheInMemory() // true
				.cacheOnDisc() // true
				.build();
		config= new ImageLoaderConfiguration.Builder(context)
		.threadPriority(Thread.NORM_PRIORITY - 2)
		.denyCacheImageMultipleSizesInMemory()
		.discCacheExtraOptions(480, 800, CompressFormat.PNG, 90,null)
		.discCacheFileNameGenerator(new Md5FileNameGenerator())
		.tasksProcessingOrder(QueueProcessingType.LIFO)
		.discCache(new UnlimitedDiscCache(FileUtils.getSdcardDir()))
		.discCacheSize(4 * 1024 * 1024)
        .discCacheFileCount(100)
        .defaultDisplayImageOptions(options )
		.build();
		ImageLoader.getInstance().init(config);
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
			Log.e("error", "" + stacktrace);
			defaultUEH.uncaughtException(t, e);
		}
	}
}
