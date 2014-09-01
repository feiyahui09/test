package com.zmax.app.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.widget.Toast;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.LocationManagerProxy;
import com.umeng.analytics.MobclickAgent;
import com.zmax.app.R;
import com.zmax.app.task.GetCityLocationTask;
import com.zmax.app.ui.base.BaseSlidingFragmentActivity;
import com.zmax.app.utils.Constant;
import com.zmax.app.utils.Log;
import com.zmax.app.utils.Utility;
import eu.inmite.android.lib.dialogs.ISimpleDialogCancelListener;
import eu.inmite.android.lib.dialogs.ISimpleDialogListener;

public class MainActivity extends BaseSlidingFragmentActivity implements ISimpleDialogCancelListener,
		ISimpleDialogListener {
	public static final int REQUEST_PROGRESS = 1;
	static final long FINISH_DURATION_TIME = 1000 * 2;
	int backPressCount = 0;
	long last_press_time = 0;
	private Context mContext;
	private DialogFragment progressDialog;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mContext = this;
		handleIntent();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		if (progressDialog != null && progressDialog.getActivity() != null) progressDialog.dismiss();
		removeStickyBroadcast(new Intent(Constant.FEEDBACK_SENDED_ACTION));
		MobclickAgent.onPause(this);
	}

	@Deprecated
	@Override
	public void switchContent(Fragment fragment) {
		// TODO Auto-generated method stub
		super.switchContent(fragment);

	}

	@Override
	public void onBackPressed() {
		// super.onBackPressed();
		if (!getSlidingMenu().isSlidingEnabled()) return;
		backPressCount++;

		if (System.currentTimeMillis() - last_press_time > FINISH_DURATION_TIME){
			last_press_time = System.currentTimeMillis();
			backPressCount = 0;
			Utility.toastResult(mContext, "再按一次，退出ZMAX ! ");
		} else if (backPressCount >= 1){
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
		switch (arg0) {
			case Constant.DialogCode.TYPE_TOKEN_ERROR:
				Constant.saveLogin(null);
				// switchContent(new PlayInZmaxLoginFragment());
				handleSeleceted(R.id.ll_menu_playzmax, true);
				break;

			default:
				break;
		}
	}

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
		Log.e("@@");
	}

	@Override
	protected void onNewIntent(Intent intent) {
		// TODO Auto-generated method stub
		super.onNewIntent(intent);
		Log.e("@@");
		setIntent(intent);
		handleIntent();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobclickAgent.onResume(this);
	}

	private boolean handleIntent() {
		boolean shouldDo = false;
		if (getIntent().getAction() != null)
			if (getIntent().getAction().equals(Constant.DialogCode.ACTION_BACK_LOGIN)){
				// switchContent(new PlayInZmaxLoginFragment());
				handleSeleceted(R.id.ll_menu_playzmax, true);
				shouldDo = true;
			} else if (getIntent().getAction().equals(Constant.DialogCode.ACTION_BACK_INDEX)){
				handleSeleceted(R.id.btn_activities, true);
				shouldDo = true;
				Toast.makeText(mContext, TextUtils.isEmpty(Constant.CUR_CITY) ? "定位失败!  为您显示默认城市信息！" : Constant
						.CUR_CITY, 1222).show();

			}
		return shouldDo;
	}


}
