package com.zmax.app.chat;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zmax.app.R;
import com.zmax.app.ui.base.BaseActivity;
import com.zmax.app.utils.Constant;
import com.zmax.app.utils.DefaultShared;
import com.zmax.app.utils.Utility;

public class ChatSettingActivity extends BaseActivity {
	private RadioButton btn_man, btn_feman;
	private Context mContext;
	private Button btn_back, btn_save;
	private TextView tv_title;
	
	private RelativeLayout rl_edit_name, rl_show_name;
	private EditText et_name;
	private TextView tv_name, tv_gender;
	private ImageView iv_clear;
	private String gender = "男";
	private String name;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.chat_setting);
		init();
	}
	
	private void init() {
		mContext = this;
		tv_title = (TextView) findViewById(R.id.tv_title);
		tv_title.setText("聊天室设置");
		btn_back = (Button) findViewById(R.id.btn_back);
		btn_back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		btn_save = (Button) findViewById(R.id.btn_share);
		btn_save.setVisibility(View.VISIBLE);
		btn_save.setBackgroundResource(R.drawable.chat_menu_sure_btn_sel);
		btn_save.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (TextUtils.isEmpty(tv_name.getText().toString().trim())) {
					Toast.makeText(mContext, "昵称不能为空哦！", 400).show();
					return;
				}
				saveSelfInfo();
				Toast.makeText(mContext, "修改个人信息成功！！", 400).show();
				finish();
				
			}
		});
		
		rl_edit_name = (RelativeLayout) findViewById(R.id.rl_edit_name);
		rl_show_name = (RelativeLayout) findViewById(R.id.rl_show_name);
		et_name = (EditText) findViewById(R.id.et_name);
		tv_name = (TextView) findViewById(R.id.tv_name);
		tv_gender = (TextView) findViewById(R.id.tv_gender);
		iv_clear = (ImageView) findViewById(R.id.iv_clear);
		
		rl_show_name.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				et_name.setText(tv_name.getText().toString().trim());
				rl_edit_name.setVisibility(View.VISIBLE);
				rl_show_name.setVisibility(View.GONE);
				
			}
		});
		
		iv_clear.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				et_name.setText("");
			}
		});
		tv_gender.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				new AlertDialog.Builder(mContext).setTitle("选择性别").setItems(R.array.gender, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						/* User clicked so do some stuff */
						String[] items = getResources().getStringArray(R.array.gender);
						gender = items[which];
						tv_gender.setText(gender);
					}
				}).show();
			}
		});
		
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		tv_name.setText(DefaultShared.getString(Constant.Chat.SELF_NAME, ""));
		tv_gender.setText(DefaultShared.getString(Constant.Chat.SELF_GENDER, ""));
	}
	
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		
		if (et_name.getVisibility() != View.VISIBLE) return super.dispatchTouchEvent(ev);
		int x = (int) ev.getRawX();
		int y = (int) ev.getRawY();
		int w = et_name.getWidth();
		int h = et_name.getHeight();
		int[] location = new int[2], locationW = new int[2];
		et_name.getLocationOnScreen(location);
		// et_name.getLocationInWindow(locationW);
		
		int sx = location[0];
		int sy = location[1];
		
		if (x > sx && x < sx + w && y < sy + h && y > sy) {
		}
		else {
			handleETNameOuside();
		}
		return super.dispatchTouchEvent(ev);
	}
	
	private void handleETNameOuside() {
		
		rl_edit_name.setVisibility(View.GONE);
		rl_show_name.setVisibility(View.VISIBLE);
		if (!Utility.isETNull(et_name)) tv_name.setText(et_name.getText().toString().trim());
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		
	}
	
	private void saveSelfInfo() {
		name = tv_name.getText().toString().trim();
		gender = tv_gender.getText().toString().trim();
		DefaultShared.putString(Constant.Chat.SELF_GENDER, gender);
		DefaultShared.putString(Constant.Chat.SELF_NAME, name);
	}
	
}
