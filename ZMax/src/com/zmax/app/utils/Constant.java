package com.zmax.app.utils;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;

import com.zmax.app.R;
import com.zmax.app.model.Act;
import com.zmax.app.ui.HotelDetailActivity;

public class Constant {
	public static final boolean LOGD_ENABLE = false;
	public static final boolean LOG_SDCARD_ENABLE = false; // 是否打印日志在SDcard上，正式发版前记得设置为false
	public static final String LOG_FILE_NAME = "com.zmax.app" + "_log.txt"; // 根据包名改变日志文件名字
	
	public static final String FANCY_URL = "http://fancy.189.cn/service/request"; // 正式环境
	// public static String FANCY_URL = "http://125.88.74.85/service/request";
	// //测试环境
	// public static String FANCY_URL =
	// "http://220.181.187.155/service/request"; //预发布环境
	public static final String TEST_ICON_URI = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQQGKJtgkUvw47FeyYwTia3chxBqChjxqKgRmX_QItRWAhVqJjc";
	
	//  百度地图定位
	public static String MAP_SDK_KEY = "seIhcSk2TiTqdHTjyGG8sjCn";// 个人
	// public static String MAP_AK = "Io8gL4Aybx3CZsdpSoKCqZPB";// 个人
	public static String MAP_AK = "nGf4WdZTlTgAjFSQ0AtuSWjc";// zmax
	
	public static String IP_LOCATION_URL = "http://api.map.baidu.com/location/ip";
	
	public static List<Object> getFalseDataObject(boolean bo) {
		
		List<Object> mList = new ArrayList<Object>();
		
		for (int i = 0; i < 10; i++) {
			Act activityDetail = new Act();
			if (bo)
				activityDetail.dt = "2";
			else
				activityDetail.dt = "12";
			mList.add(activityDetail);
			
		}
		
		return mList;
	}
	
	public static List<Act> getFalseData(boolean bo) {
		
		List<Act> mList = new ArrayList<Act>();
		
		for (int i = 0; i < 30; i++) {
			Act activityDetail = new Act();
			if (bo)
				activityDetail.dt = "2";
			else
				activityDetail.dt = "12";
			mList.add(activityDetail);
			
		}
		
		return mList;
	}
	
	public static List<Act> getFalseData(int count) {
		
		List<Act> mList = new ArrayList<Act>();
		
		for (int i = 0; i < count; i++) {
			Act activityDetail = new Act();
			
			activityDetail.dt = "12";
			mList.add(activityDetail);
			
		}
		
		return mList;
	}
	
	public static List<View> getHotelFalseDataView(final FragmentActivity fragmentActivity, LayoutInflater inflater) {
		
		List<View> mList = new ArrayList<View>();
		
		for (int i = 0; i < 12; i++) {
			View view = inflater.inflate(R.layout.hotel_book_list_item, null);
			
			view.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					fragmentActivity.startActivity(new Intent(fragmentActivity, HotelDetailActivity.class));
					
				}
			});
			mList.add(view);
			
		}
		
		return mList;
	}
	
}
