package com.zmax.app.chat;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.zmax.app.R;
import com.zmax.app.ui.base.BaseActivity;
import com.zmax.app.utils.Constant;
import com.zmax.app.utils.DefaultShared;
import com.zmax.app.utils.Utility;

public class ChatIndexActivity extends BaseActivity {
	private Context mContext;
	private EditText et_nick_name;
	private Button btn_Back, btn_Share, btn_login;
	private TextView tv_title;
	private RadioGroup rg_gender;
	private RadioButton btn_feman, btn_man;
	
	private String gender = "男";
	private String name;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.chat_index);
		init();
	}
	
	private void init() {
		mContext = this;
		et_nick_name = (EditText) findViewById(R.id.et_nick_name);
		et_nick_name.setText(DefaultShared.getString(Constant.Chat.SELF_NAME, ""));
		tv_title = (TextView) findViewById(R.id.tv_title);
		tv_title.setText("聊天室");
		btn_Back = (Button) findViewById(R.id.btn_back);
		
		btn_Back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		btn_Share = (Button) findViewById(R.id.btn_share);
		btn_Share.setVisibility(View.VISIBLE);
		btn_Share.setBackgroundResource(R.drawable.chat_more_menu_sel);
		btn_Share.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivity(new Intent(mContext, ChatMoreActivity.class));
			}
		});
		
		btn_login = (Button) findViewById(R.id.btn_login);
		btn_login.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (Utility.isETNull(et_nick_name)) {
					Toast.makeText(mContext, "昵称不能为空哦！", 500).show();
					return;
				}
				name = et_nick_name.getText().toString().trim();
				saveSelfInfo();
				startActivity(new Intent(mContext, ChatRoomActivity.class));
				
			}
		});
		
		rg_gender = (RadioGroup) findViewById(R.id.rg_gender);
		btn_feman = (RadioButton) findViewById(R.id.btn_feman);
		btn_man = (RadioButton) findViewById(R.id.btn_man);
		
		rg_gender.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				if (checkedId == R.id.btn_feman) {
					gender = "女";
				}
				else {
					gender = "男";
				}
			}
		});
		
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		if (DefaultShared.getString(Constant.Chat.SELF_GENDER, "男").equals("男"))
			btn_man.setChecked(true);
		else
			btn_feman.setChecked(true);
	}
	
	private void saveSelfInfo() {
		DefaultShared.putString(Constant.Chat.SELF_GENDER, gender);
		DefaultShared.putString(Constant.Chat.SELF_NAME, name);
		
	}
	
}
