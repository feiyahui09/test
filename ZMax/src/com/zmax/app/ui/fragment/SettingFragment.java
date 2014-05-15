package com.zmax.app.ui.fragment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.zmax.app.R;
import com.zmax.app.ZMaxApplication;
import com.zmax.app.model.Update;
import com.zmax.app.net.NetWorkHelper;
import com.zmax.app.task.CheckUpdateTask;
import com.zmax.app.ui.AboutActivity;
import com.zmax.app.ui.DocumentsActivity;
import com.zmax.app.ui.FeedBackActivity;
import com.zmax.app.ui.WelcomeActivity;
import com.zmax.app.utils.Constant;
import com.zmax.app.utils.Log;
import com.zmax.app.utils.Utility;

public class SettingFragment extends Fragment implements OnClickListener {
	
	private Button btn_feedback, btn_check_update, btn_welcome, btn_user_regulation, btn_play_zmax, btn_member, btn_about;
	private CheckUpdateTask checkUpdateTask;
	static Handler mHandler;
	
	public SettingFragment() {
		setRetainInstance(true);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.setting, null);
		btn_feedback = (Button) view.findViewById(R.id.btn_feedback);
		btn_feedback.setOnClickListener(this);
		btn_check_update = (Button) view.findViewById(R.id.btn_check_update);
		btn_check_update.setOnClickListener(this);
		btn_welcome = (Button) view.findViewById(R.id.btn_welcome);
		btn_welcome.setOnClickListener(this);
		btn_user_regulation = (Button) view.findViewById(R.id.btn_user_regulation);
		btn_user_regulation.setOnClickListener(this);
		btn_play_zmax = (Button) view.findViewById(R.id.btn_play_zmax);
		btn_play_zmax.setOnClickListener(this);
		btn_member = (Button) view.findViewById(R.id.btn_member);
		btn_member.setOnClickListener(this);
		btn_about = (Button) view.findViewById(R.id.btn_about);
		btn_about.setOnClickListener(this);
		
		return view;
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}
	
	boolean isChechUpdateFinished = true;
	
	@Override
	public void onClick(View v) {
		Intent intent = new Intent();
		switch (v.getId()) {
		
			case R.id.btn_feedback:
				intent.setClass(getActivity(), FeedBackActivity.class);
				startActivity(intent);
				break;
			case R.id.btn_check_update:
				if (isChechUpdateFinished) checkUpdate();
				break;
			case R.id.btn_welcome:
				intent.setClass(getActivity(), WelcomeActivity.class);
				intent.setAction(Constant.ACTION_WELCOME_FROM_SETTING);
				startActivity(intent);
				break;
			case R.id.btn_user_regulation:
				intent.setClass(getActivity(), DocumentsActivity.class);
				intent.putExtra(Constant.Documents.DOCUMENTS_TYPE_KEY, Constant.Documents.PROTOCAL_TYPE);
				startActivity(intent);
				break;
			case R.id.btn_play_zmax:
				intent.setClass(getActivity(), DocumentsActivity.class);
				intent.putExtra(Constant.Documents.DOCUMENTS_TYPE_KEY, Constant.Documents.GUIDE_TYPE);
				startActivity(intent);
				break;
			case R.id.btn_member:
				intent.setClass(getActivity(), DocumentsActivity.class);
				intent.putExtra(Constant.Documents.DOCUMENTS_TYPE_KEY, Constant.Documents.RIGHT_TYPE);
				startActivity(intent);
				break;
			case R.id.btn_about:
				intent.setClass(getActivity(), AboutActivity.class);
				startActivity(intent);
				break;
			
			default:
				break;
		}
		
	}
	
	@Override
	public void onResume() {
		super.onResume();
		isChechUpdateFinished = true;
	}
	
	private void checkUpdate() {
		if (!NetWorkHelper.checkNetState(getActivity())) {
			Utility.toastNetworkFailed(getActivity());
			return;
		}
		
		isChechUpdateFinished = false;
		checkUpdateTask = new CheckUpdateTask(getActivity(), new CheckUpdateTask.TaskCallBack() {
			
			@Override
			public void onCallBack(final Update result) {
				isChechUpdateFinished = true;
				if (getActivity() == null) return;
				if (result == null) return;
				if (result.status == 200) {
					ProgressDialog progressDialog = new ProgressDialog(getActivity());
					progressDialog.setTitle("提示");
					progressDialog.setMessage("正在更新房间控制信息中！");
					// progressDialog.show();
					new AlertDialog.Builder(getActivity()).setTitle("提示").setMessage(result.description)
							.setPositiveButton("立即更新", new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {
									dialog.dismiss();
									goUpdate(result.package_name);
									// goUpdate("http://immomo.com/download/momo.apk");
									
								}
							}).setNegativeButton("暂不更新", new DialogInterface.OnClickListener() {
								
								@Override
								public void onClick(DialogInterface dialog, int which) {
									dialog.dismiss();
								}
							}).show();
					
				}
				else if (result.status == 304) {
					Toast.makeText(getActivity(), result.message, 300).show();
				}
				else {
					Toast.makeText(getActivity(), result.message, 300).show();
				}
			}
		});
		
		String curVersion = getVersionName();
		// .replace(".", "");
		checkUpdateTask.execute(curVersion);
	}
	
	private void goUpdate(final String apkUrl) {
		
		if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			Toast.makeText(getActivity(), "SD卡不可用，无法下载安装包！", 300).show();
			return;
		}
		
		final NotificationManager mNotifyManager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
		final NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getActivity());
		mBuilder.setContentTitle("正在下载新版本").setSmallIcon(R.drawable.ic_launcher_4notification);
		mHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				if (msg.what == 1) {
					if (msg.arg1 < 100) {
						mBuilder.setProgress(100, msg.arg1, false);
						// Displays the progress bar for the first time.
						mNotifyManager.notify(0, mBuilder.build());
					}
					else {
						// When the loop is finished, updates the notification
						mBuilder.setContentTitle("下载完成！").setContentText("新版本已下载完成，请点击安装！")
						// Removes the progress bar
								.setProgress(0, 0, false).setAutoCancel(true);
						Intent inty = new Intent(Intent.ACTION_VIEW);
						inty.setDataAndType(Uri.parse("file://" + getApkPath()), "application/vnd.android.package-archive");
						inty.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						// context.startActivity(intent);
						PendingIntent intent = PendingIntent.getActivity(ZMaxApplication.getInstance(), 0, inty, 0);
						mBuilder.setContentIntent(intent);
						
						mNotifyManager.notify(0, mBuilder.build());
					}
				}
				
			}
		};
		new Thread(new Runnable() {
			@Override
			public void run() {
				downloadApk(apkUrl, getApkPath());
			}
		}
		// Starts the thread by calling the run() method in its Runnable
		).start();
		
	}
	
	public static String getApkPath() {
		String apkPath = Environment.getExternalStorageDirectory() + "/zmax.apk";
		return apkPath;
		
	}
	
	private static boolean downloadApk(String pageUrl, String apkName) {
		boolean result = false;
		File file = null;
		OutputStream output = null;
		try {
			URL url = new URL(pageUrl);
			HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
			urlConn.setDoInput(true);
			urlConn.connect();
			
			InputStream inputStream = urlConn.getInputStream();
			
			int totalLen = urlConn.getContentLength();
			int count = 0;
			file = new File(apkName);
			if (!file.exists()) file.createNewFile();
			output = new FileOutputStream(file);
			byte buffer[] = new byte[4 * 1024];
			int len = 0, old_progress = 0, progress = 0;
			while ((len = inputStream.read(buffer)) != -1) {
				count += len;
				progress = (int) (((float) count / totalLen) * 100);
				
				// 更新进度
				Message msg = new Message();
				msg.what = 1;
				msg.arg1 = progress;
				Log.i("VersionUpgradeUitl--progress", progress + "");
				if (progress - old_progress >= 1) {
					old_progress = progress;
					mHandler.sendMessage(msg);
				}
				output.write(buffer, 0, len);
			}
			output.flush();
			
			result = true;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			try {
				if (null != output) output.close();
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	
	private String getVersionName() {
		String version = "";
		try {
			
			// 获取packagemanager的实例
			PackageManager packageManager = getActivity().getPackageManager();
			// getPackageName()是你当前类的包名，0代表是获取版本信息
			PackageInfo packInfo = packageManager.getPackageInfo("com.zmax.app", 0);
			version = packInfo.versionName;
		}
		catch (Exception e) {
			// TODO: handle exception
			
		}
		return version;
	}
}
