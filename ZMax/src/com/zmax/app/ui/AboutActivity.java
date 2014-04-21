package com.zmax.app.ui;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.zmax.app.R;
import com.zmax.app.model.Documents;
import com.zmax.app.net.NetWorkHelper;
import com.zmax.app.task.GetDocumentsTask;
import com.zmax.app.ui.base.BaseActivity;
import com.zmax.app.utils.Constant;
import com.zmax.app.utils.DefaultShared;
import com.zmax.app.utils.JsonMapperUtils;

public class AboutActivity extends BaseActivity {
	
	private Button btn_Back;
	private TextView tv_title;
	private TextView tv_version, btn_click_us;
	private Context context;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about);
		init();
		initHeader();
		
	}
	
	private void init() {
		context = this;
		tv_version = (TextView) findViewById(R.id.tv_version);
		tv_version.setText(getVersionName());
		btn_click_us = (Button) findViewById(R.id.btn_click_us);
		btn_click_us.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	
	private void initHeader() {
		findViewById(R.id.btn_share).setVisibility(View.GONE);
		btn_Back = (Button) findViewById(R.id.btn_back);
		btn_Back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}
	
	private String getVersionName() {
		String version = "";
		try {
			
			// 获取packagemanager的实例
			PackageManager packageManager = getPackageManager();
			// getPackageName()是你当前类的包名，0代表是获取版本信息
			PackageInfo packInfo = packageManager.getPackageInfo("com.zmax.app", 0);
			version = packInfo.versionName;
		}
		catch (Exception e) {
			// TODO: handle exception
			
		}
		return "v"+version;
	}
}
