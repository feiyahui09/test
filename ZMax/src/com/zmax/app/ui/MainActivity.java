package com.zmax.app.ui;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.zmax.app.R;
import com.zmax.app.model.CityLocation;
import com.zmax.app.task.GetCityLocationTask;
import com.zmax.app.ui.base.BaseSlidingFragmentActivity;
import com.zmax.app.ui.fragment.ActListFragment;
import com.zmax.app.ui.fragment.NetErrorFragment;
import com.zmax.app.ui.fragment.PlayInZmaxFragment.PlayZmaxLogoutCallback;
import com.zmax.app.ui.fragment.PlayInZmaxLoginFragment;
import com.zmax.app.utils.Constant;
import com.zmax.app.utils.DefaultShared;
import com.zmax.app.utils.PhoneUtil;

public class MainActivity extends BaseSlidingFragmentActivity implements PlayZmaxLogoutCallback {
	private Context mContext;
	private GetCityLocationTask locationTask;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mContext = this;
		if (!PhoneUtil.isNetworkOk(mContext)) {
			switchContent(new NetErrorFragment());
			return;
		}
		initLocate();
	}
	
	private void initLocate() {
		locationTask = new GetCityLocationTask(this, new GetCityLocationTask.TaskCallBack() {
			
			@Override
			public void onCallBack(CityLocation result) {
				if (mContent instanceof ActListFragment) return;
				if (result != null && !TextUtils.isEmpty(result.city) && !isFinishing()) {
					String cityStr = result.city.replace("市", "");
					Constant.CUR_CITY = cityStr;
					DefaultShared.putString(Constant.SPFKEY.CITY_LOCATION_KEY, cityStr);
					switchContent(new ActListFragment());
					Toast.makeText(mContext, "   " + result.province + cityStr, 2222).show();
				}
				else if (!PhoneUtil.isNetworkOk(mContext)) {
					switchContent(new NetErrorFragment());
					return;
				}
				else {
					// 显示默认列表
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
	}
	
	@Override
	public void onLogoutViewCreate() {
		btn_more.setVisibility(View.VISIBLE);
		btn_more.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				switchContent(new PlayInZmaxLoginFragment());
			}
		});
	}
	
	@Override
	public void onLogoutViewDestroy() {
		btn_more.setVisibility(View.GONE);
		
	}
	
}
