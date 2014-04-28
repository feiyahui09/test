package com.zmax.app.chat;

import android.app.ProgressDialog;
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
import com.zmax.app.model.BaseModel;
import com.zmax.app.net.NetWorkHelper;
import com.zmax.app.task.ModifyChatUserInfoTask;
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
	
	private int gender = 1;
	private String name;
	private ProgressDialog progressDialog;
	private ModifyChatUserInfoTask modifyChatUserInfoTask;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.chat_index);
		init();
	}
	
	private void init() {
		mContext = this;
		if (Constant.getLogin() == null) {
			Utility.toastFailedResult(mContext);
			finish();
			return;
		}
		// user_id = getIntent().getIntExtra(Constant.Chat.SELF_ID, -1);
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
					Utility.toastResult(mContext, "昵称不能为空哦！");
					return;
				}
				name = et_nick_name.getText().toString().trim();
				goVertify(mContext, Constant.getLogin().user_id + "", gender, name);
			}
		});
		
		rg_gender = (RadioGroup) findViewById(R.id.rg_gender);
		btn_feman = (RadioButton) findViewById(R.id.btn_feman);
		btn_man = (RadioButton) findViewById(R.id.btn_man);
		
		rg_gender.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				if (checkedId == R.id.btn_feman) {
					gender = 0;
				}
				else {
					gender = 1;
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
	
	@Deprecated
	private void saveSelfInfo() {
		DefaultShared.putInt(Constant.Chat.SELF_GENDER, gender);
		DefaultShared.putString(Constant.Chat.SELF_NAME, name);
		
	}
	
	private void goVertify(final Context context, String _user_id, final int _gender, final String _nick_name) {
		progressDialog = new ProgressDialog(context);
		progressDialog.setTitle("提示");
		progressDialog.setCancelable(false);
		progressDialog.setMessage("正在检查昵称是否被使用！");
		progressDialog.show();
		modifyChatUserInfoTask = new ModifyChatUserInfoTask(context, new ModifyChatUserInfoTask.TaskCallBack() {
			@Override
			public void onCallBack(BaseModel result) {
				if (progressDialog != null && progressDialog.isShowing()) progressDialog.cancel();
				if (result == null) {
					if (NetWorkHelper.checkNetState(context))
						Utility.toastNetworkFailed(context);
					else
						Utility.toastFailedResult(context);
				}
				else if (result.status != 200) {
					Utility.toastResult(context, result.message);
				}
				else {
					Utility.toastResult(mContext, "恭喜！你可以使用该昵称！");
					// saveSelfInfo();
					Constant.modifyLogin(_gender, _nick_name);
					handler.postDelayed(new Runnable() {
						@Override
						public void run() {
							startActivity(new Intent(mContext, ChatRoomActivity.class));
							finish();
						}
					}, 500);
				}
			}
		});
		modifyChatUserInfoTask.execute(Constant.getLogin().user_id + "", _gender + "", _nick_name);
	}
}
