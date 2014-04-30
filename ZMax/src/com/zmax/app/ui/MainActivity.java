package com.zmax.app.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.zmax.app.model.CityLocation;
import com.zmax.app.task.GetCityLocationTask;
import com.zmax.app.ui.base.BaseSlidingFragmentActivity;
import com.zmax.app.ui.fragment.ActListFragment;
import com.zmax.app.ui.fragment.PlayInZmaxLoginFragment;
import com.zmax.app.utils.Constant;
import com.zmax.app.utils.DefaultShared;
import com.zmax.app.utils.Utility;

import eu.inmite.android.lib.dialogs.ISimpleDialogCancelListener;
import eu.inmite.android.lib.dialogs.ISimpleDialogListener;
import eu.inmite.android.lib.dialogs.ProgressDialogFragment;

public class MainActivity extends BaseSlidingFragmentActivity implements ISimpleDialogCancelListener, ISimpleDialogListener {
	private Context mContext;
	private GetCityLocationTask locationTask;
	private DialogFragment progressDialog;
	public static final int REQUEST_PROGRESS = 1;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mContext = this;
		// if (!PhoneUtil.isNetworkOk(mContext)) {
		// switchContent(new NetErrorFragment());
		// return;
		// }
		initLocate();
	}
	
	private void initLocate() {
		progressDialog = ProgressDialogFragment.createBuilder(this, getSupportFragmentManager()).setMessage("正在定位中...")
				.setRequestCode(REQUEST_PROGRESS).setTitle("提示").setCancelable(true).show();
		
		locationTask = new GetCityLocationTask(this, new GetCityLocationTask.TaskCallBack() {
			
			@Override
			public void onCallBack(CityLocation result) {
				if (progressDialog != null && progressDialog.getActivity() != null) progressDialog.dismiss();
				
				if (mContent instanceof ActListFragment) return;
				if (result != null && !TextUtils.isEmpty(result.city) && !isFinishing()) {
					String cityStr = result.city.replace("市", "");
					Constant.CUR_CITY = cityStr;
					DefaultShared.putString(Constant.SPFKEY.CITY_LOCATION_KEY, cityStr);
					switchContent(new ActListFragment());
					Toast.makeText(mContext, "   " + result.province + cityStr, 2222).show();
				}
				// else if (!PhoneUtil.isNetworkOk(mContext)) {
				// switchContent(new NetErrorFragment());
				// return;
				// }
				else {
					
					Constant.CUR_CITY = "武汉";
					DefaultShared.putString(Constant.SPFKEY.CITY_LOCATION_KEY, "武汉");
					switchContent(new ActListFragment());
					// 显示默认列表
					Toast.makeText(mContext, "城市定位失败！   为您显示默认城武汉市信息！", 2222).show();
					
				}
			}
		});
		locationTask.execute(Constant.MAP_AK);
		
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		if (locationTask != null) locationTask.cancel(true);
		if (progressDialog != null && progressDialog.getActivity() != null) progressDialog.dismiss();
		
	}
	
	public void showLogoutView() {
		btn_share.setVisibility(View.VISIBLE);
		btn_share.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Constant.saveLogin(null);
				switchContent(new PlayInZmaxLoginFragment());
			}
		});
	}
	
	public void hideLogoutView() {
		btn_share.setVisibility(View.GONE);
		
	}
	
	int backPressCount = 0;
	long last_press_time = 0;
	static final long FINISH_DURATION_TIME = 1000 * 2;
	
	@Override
	public void onBackPressed() {
		// super.onBackPressed();
		if (!getSlidingMenu().isSlidingEnabled()) return;
		backPressCount++;
		
		if (System.currentTimeMillis() - last_press_time > FINISH_DURATION_TIME) {
			last_press_time = System.currentTimeMillis();
			backPressCount = 0;
			Utility.toastResult(mContext, "再按一次，退出ZMAX ! ");
		}
		else if (backPressCount >= 1) {
			finish();
		}
	}
	
	@Override
	public void onCancelled(int arg0) {
		
	}
	
	@Override
	public void onNegativeButtonClicked(int arg0) {
		
	}
	
	@Override
	public void onPositiveButtonClicked(int arg0) {
		
	}
}
