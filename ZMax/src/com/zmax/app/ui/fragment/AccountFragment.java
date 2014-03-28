package com.zmax.app.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.zmax.app.R;

public class AccountFragment extends Fragment implements OnClickListener {
	
	private Button btn_confirm;
	private EditText et_account, et_password;
	private TextView tv_forget_pw, tv_regist;
	
	public AccountFragment() {
		this(R.color.white);
	}
	
	public AccountFragment(int colorRes) {
		setRetainInstance(true);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.account_login, null);
		btn_confirm = (Button) view.findViewById(R.id.btn_confirm);
		btn_confirm.setOnClickListener(this);
		et_account = (EditText) view.findViewById(R.id.et_account);
		et_account.setOnClickListener(this);
		et_password = (EditText) view.findViewById(R.id.et_password);
		et_password.setOnClickListener(this);
		tv_forget_pw = (TextView) view.findViewById(R.id.tv_forget_pw);
		tv_forget_pw.setOnClickListener(this);
		tv_regist = (TextView) view.findViewById(R.id.tv_regist);
		tv_regist.setOnClickListener(this);
		
		return view;
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		
			case R.id.tv_regist:
				
				break;
			case R.id.tv_forget_pw:
				
				break;
			case R.id.et_password:
				
				break;
			case R.id.et_account:
				
				break;
			case R.id.btn_confirm:
				
				break;
			
			default:
				break;
		}
		
	}
}
