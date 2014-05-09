package com.zmax.app.manage;

import java.util.List;

import com.zmax.app.model.Act;
import com.zmax.app.model.ActList;
import com.zmax.app.model.CityLocation;
import com.zmax.app.model.FeeBack;
import com.zmax.app.net.NetAccessor;
import com.zmax.app.net.NetWorkHelper;
import com.zmax.app.ui.fragment.ActListFragment;
import com.zmax.app.utils.Constant;
import com.zmax.app.utils.Utility;

import android.app.IntentService;
import android.content.Intent;
import android.os.Handler;
import android.text.TextUtils;
import android.widget.Toast;

public class DataInitalService extends IntentService {
	public static final String CONTACT_CONTENT = "contact_content";
	public static final String ADVISE_CONTENT = "advise_content";
	Handler handler;
	
	public DataInitalService() {
		this("DataInitalService");
	}
	
	public DataInitalService(String name) {
		super(name);
		handler = new Handler(getMainLooper());
		
	}
	
	@Override
	protected void onHandleIntent(Intent intent) {
		Constant.isHotellistEnd = false;
		Constant.isActlistEnd = false;
		Constant.isLocateEnd = false;
		
		/**
		 * 定位
		 */
		CityLocation cityLocation = NetAccessor.getCityLoacationByIp(this, Constant.MAP_AK);
		Constant.isLocateEnd = true;
		String toastStr = "";
		if (cityLocation != null && !TextUtils.isEmpty(cityLocation.city)) {
			String cityStr = cityLocation.city.replace("市", "");
			Constant.CUR_CITY = cityStr;
			toastStr = "   " + cityLocation.province + cityStr;
		}
		else {
			Constant.CUR_CITY = "";
			toastStr = "定位失败!  为您显示默认城市信息！";
		}
		final String tmp = toastStr;
		handler.post(new Runnable() {
			
			@Override
			public void run() {
				Toast.makeText(DataInitalService.this, tmp, 2222).show();
			}
		});
		
		// /**
		// * 首页活动列表
		// */
		//
		// ActList result = NetAccessor.getActList(this, Constant.CUR_CITY,
		// String.valueOf(1), "" + Constant.PER_NUM_GET_ACTLIST);
		// Constant.isActlistEnd = true;
		// if (result != null && result.status == 200) {
		// final List<Act> actList = result.events;
		// if (curPage == 1) {
		// adapter.Clear();
		// new Thread(new Runnable() {
		// @Override
		// public void run() {
		// DataManage.saveIndexActlist2DB(actList);
		// }
		// }).start();
		// }
		// if (!actList.isEmpty()) {
		// adapter.appendToList(actList);
		// listview.onLoad();
		// curPage++;
		// }
		// else {
		// Utility.toastNoMoreResult(getActivity());
		// listview.onLoads();
		// }
		// }
		// else {
		// if (curPage == 1)
		// adapter.appendToList(DataManage.getIndexActlist4DB());
		//
		// if (!NetWorkHelper.checkNetState(getActivity())) {
		// Utility.toastNetworkFailed(getActivity());
		// }
		// else if (result != null)
		// Utility.toastResult(getActivity(), result.message);
		// else
		// Utility.toastFailedResult(getActivity());
		// listview.onLoads();
		// }
		//
		Intent it = new Intent(Constant.FEEDBACK_SENDED_ACTION);
		sendBroadcast(it);
	}
}
