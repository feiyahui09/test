package com.zmax.app.ui;

import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;

import com.zmax.app.R;
import com.zmax.app.model.CityLocation;
import com.zmax.app.task.GetCityLocationTask;
import com.zmax.app.ui.base.BaseSlidingFragmentActivity;
import com.zmax.app.ui.fragment.ActListFragment;
import com.zmax.app.ui.fragment.NetErrorFragment;
import com.zmax.app.utils.Constant;
import com.zmax.app.utils.PhoneUtil;

public class MainActivity extends BaseSlidingFragmentActivity {
	private Context mContext;

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
		new GetCityLocationTask(this, new GetCityLocationTask.TaskCallBack() {

			@Override
			public void onCallBack(CityLocation result) {
				if (result != null&&!isFinishing()) {
					switchContent(new ActListFragment(R.color.red));
					Toast.makeText(mContext,
							"   " + result.province + result.city, 2222).show();

				} else if (!PhoneUtil.isNetworkOk(mContext)) {
					switchContent(new NetErrorFragment());
					return;
				} else {
					// 显示默认列表
				}

			}
		}).execute(Constant.MAP_AK);

	}

}
