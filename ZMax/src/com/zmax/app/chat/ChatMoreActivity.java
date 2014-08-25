package com.zmax.app.chat;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import com.zmax.app.R;
import com.zmax.app.ui.MainActivity;
import com.zmax.app.ui.base.BaseActivity;
import com.zmax.app.utils.Constant;
import com.zmax.app.utils.FileUtil;
import com.zmax.app.utils.Log;
import com.zmax.app.utils.Utility;

import java.io.File;

public class ChatMoreActivity extends BaseActivity {
	private RadioButton btn_man, btn_feman;
	private Context mContext;
	private EditText et_nick_name;
	private Button btn_Back, btn_quit, btn_edit_name, btn_clear;
	private TextView tv_title, tv_name, tv_clear_info;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.chat_more);
		init();
	}
	
	// 清除聊天记录，昵称
	private void init() {
		mContext = this;
		et_nick_name = (EditText) findViewById(R.id.et_nick_name);
		tv_title = (TextView) findViewById(R.id.tv_title);
		btn_Back = (Button) findViewById(R.id.btn_back);
		btn_clear = (Button) findViewById(R.id.btn_clear);
		tv_name = (TextView) findViewById(R.id.tv_name);
		btn_quit = (Button) findViewById(R.id.btn_quit);
		btn_edit_name = (Button) findViewById(R.id.btn_edit_name);
		tv_clear_info = (TextView) findViewById(R.id.tv_clear_info);
		
		tv_title.setText("聊天室设置");
		btn_Back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		btn_quit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent=new Intent(mContext, MainActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				intent.setAction(Constant.DialogCode.ACTION_BACK_INDEX);
				startActivity(intent);
				finish();
			}
		});
		btn_edit_name.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(mContext, ChatSettingActivity.class));

			}
		});
		
		tv_clear_info.setText(getCacheSize());
		btn_clear.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				clearFiles();
			}
		});
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		tv_name.setText(Constant.getLogin().nick_name);
		
	}
	
	private String getCacheSize() {
		File imgfile = new File(Constant.Chat.CHAT_IMG_CACHE_ABS_PATH);
		Long size = FileUtil.getDirSize(imgfile);
		String sizeStr = FileUtil.formatFileSizeByM(size);
		Log.i("size: " + size);
		Log.i("sizeStr: " + sizeStr);
		return sizeStr;
		
	}
	
	private void clearFiles() {
		
		final Handler handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);
				
				if (msg.what == 1) {
					tv_clear_info.setText("0.00M");
					Utility.toastResult(mContext, "清除缓存成功！");
				}
				else {
					Utility.toastResult(mContext, "清除缓存失败！");
				}
			}
		};
		new Thread(new Runnable() {
			@Override
			public void run() {
				int result = -1;
				if (FileUtil.deleteDirectory(Constant.Chat.CHAT_IMG_CACHE_PATH, false)) {
					result = 1;
				}
				handler.sendEmptyMessage(result);
				
			}
		}).start();
		
	}
}
