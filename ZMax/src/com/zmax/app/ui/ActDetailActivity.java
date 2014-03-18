package com.zmax.app.ui;

import java.io.File;
import java.io.FileOutputStream;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.zmax.app.R;
import com.zmax.app.adapter.ActDetailAdapter;
import com.zmax.app.ui.base.BaseFragmentActivity;
import com.zmax.app.ui.fragment.ActDetailFirstFragment;
import com.zmax.app.ui.fragment.ActDetailSecondFragment;
import com.zmax.app.ui.fragment.ActDetailThirdFragment;
import com.zmax.app.utils.FileUtils;
import com.zmax.app.widget.ShareDialog;

public class ActDetailActivity extends BaseFragmentActivity {

	private Button btn_Back, btn_Share;

	private ViewPager pager;
	private ActDetailAdapter adapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_detail);
		initHeader();
		init();
		initData();
	}

	private void init() {
		pager = (ViewPager) findViewById(R.id.pager);

		adapter = new ActDetailAdapter(this);
		pager.setAdapter(adapter);

	}

	private void initData() {

		adapter.addTab(new ActDetailFirstFragment(R.color.red));
		adapter.addTab(new ActDetailSecondFragment());
		adapter.addTab(new ActDetailThirdFragment(R.color.white));
	}

	private void initHeader() {
		btn_Back = (Button) findViewById(R.id.btn_back);
		btn_Share = (Button) findViewById(R.id.btn_share);

		btn_Back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				new ShareDialog(ActDetailActivity.this).show();
			}
		});
		btn_Share.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
 			share();
				
			}
		});

	}

	private void share() {

		Intent sendIntent = new Intent(Intent.ACTION_SEND);
		sendIntent.setType("text/plain");
		if (Environment.MEDIA_MOUNTED.equals(Environment
				.getExternalStorageState())) {
			try {

				sendIntent.setType("image/jpeg");
				File tempFile = new File(FileUtils
						.getSDRoot(), "share.jpeg");
				if (!tempFile.exists()) {
					tempFile.createNewFile();
					FileOutputStream fOut = new FileOutputStream(
							tempFile);
					Bitmap bm = BitmapFactory.decodeResource(
							getResources(), R.drawable.ic_launcher);
					bm.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
					fOut.flush();
					fOut.close();
				}
				sendIntent.putExtra(Intent.EXTRA_STREAM, Uri
						.fromFile(tempFile));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		sendIntent.putExtra(Intent.EXTRA_SUBJECT, "分享应用");
		sendIntent
				.putExtra(Intent.EXTRA_TEXT,
						"一款不错的微博和人人网同步更新工具，http://fancy.189.cn/portal/app/download/71559");
		sendIntent
				.putExtra("sms_body",
						"一款不错的微博和人人网同步更新工具，http://fancy.189.cn/portal/app/download/71559");
		startActivity(Intent.createChooser(sendIntent, "分享应用"));
	}

}
