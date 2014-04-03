package com.zmax.app.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.sql.Timestamp;

import android.os.Environment;

import com.zmax.app.BuildConfig;

/**
 * @date 21.06.2012
 * @author Mustafa Ferhan Akman
 * 
 *         Create a simple and more understandable Android logs.
 * */

public class Log {
	
	
	static String className;
	static String methodName;
	static int lineNumber;
	static final boolean LOG_SDCARD_ENABLE = true;// 是否打印sd卡
	
	private Log() {
		/* Protect from instantiations */
	}
	
	/**
	 * 判断是否是处在debug模式下，只有在这种模式下才打印日志;
	 * 
	 * @return
	 */
	public static boolean isDebuggable() {
		return BuildConfig.DEBUG && true;
	}
	
	private static String createLog(String log) {
		
		StringBuffer buffer = new StringBuffer();
		buffer.append("[");
		buffer.append(methodName);
		buffer.append(":");
		buffer.append(lineNumber);
		buffer.append("]");
		buffer.append(log);
		
		return buffer.toString();
	}
	
	private static void getMethodNames(StackTraceElement[] sElements) {
		className = sElements[1].getFileName();
		methodName = sElements[1].getMethodName();
		lineNumber = sElements[1].getLineNumber();
	}
	
	public static void e(String message) {
		if (!isDebuggable()) return;
		
		// Throwable instance must be created before any methods
		getMethodNames(new Throwable().getStackTrace());
		String msg = createLog(message);
		fileLog(className, msg);
		android.util.Log.e(className, msg);
	}
	
	public static void i(String message) {
		if (!isDebuggable()) return;
		
		getMethodNames(new Throwable().getStackTrace());
		String msg = createLog(message);
		fileLog(className, msg);
		android.util.Log.i(className, msg);
	}
	
	public static void d(String message) {
		if (!isDebuggable()) return;
		
		getMethodNames(new Throwable().getStackTrace());
		String msg = createLog(message);
		fileLog(className, msg);
		android.util.Log.d(className, msg);
	}
	
	public static void v(String message) {
		if (!isDebuggable()) return;
		
		getMethodNames(new Throwable().getStackTrace());
		String msg = createLog(message);
		fileLog(className, msg);
		android.util.Log.v(className, msg);
	}
	
	public static void w(String message) {
		if (!isDebuggable()) return;
		/**
		 * new Throwable().getStackTrace() 跟踪应用的堆栈的信息
		 */
		getMethodNames(new Throwable().getStackTrace());
		String msg = createLog(message);
		fileLog(className, msg);
		android.util.Log.w(className, msg);
	}
	
	public static void wtf(String message) {
		if (!isDebuggable()) return;
		getMethodNames(new Throwable().getStackTrace());
		String msg = createLog(message);
		fileLog(className, msg);
		android.util.Log.wtf(className, msg);
	}
	
	/**
	 * 打印日志到文件
	 * 
	 * @param tag
	 * @param msg
	 * @param logFileName
	 */
	public static void fileLog(String tag, String msg) {
		if (!Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
			return;
		}
		try {
			File logFile = new File(Environment.getExternalStorageDirectory(), Constant.LOG_FILE_NAME);
			if (!logFile.exists()) {
				logFile.createNewFile();
			}
			BufferedWriter bw = new BufferedWriter(new FileWriter(logFile, true));
			bw.append(new Timestamp(System.currentTimeMillis()).toString()).append("---").append(tag).append("---").append(msg)
					.append("\n");
			bw.close();
		}
		catch (Exception e) {
		}
	}
}
