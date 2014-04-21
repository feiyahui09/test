package com.zmax.app.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.zmax.app.R;
import com.zmax.app.model.Act;
import com.zmax.app.ui.HotelDetailActivity;

public class Constant {
	public static final boolean LOGD_ENABLE = false;
	public static final boolean LOG_SDCARD_ENABLE = false; // 是否打印日志在SDcard上，正式发版前记得设置为false
	public static final String LOG_FILE_NAME = "com.zmax.app" + "_log.txt"; // 根据包名改变日志文件名字
	
	//public static final String ZMAX_URL = "http://zmax.bestapp.us/api/v1/"; // 正式环境
	public static String ZMAX_URL = "http://zmaxtest.bestapp.us/api/v1/";
	// //测试环境
	// public static String FANCY_URL =
	// "http://220.181.187.155/service/request"; //预发布环境
	public static final String TEST_ICON_URI = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQQGKJtgkUvw47FeyYwTia3chxBqChjxqKgRmX_QItRWAhVqJjc";
	
	//  百度地图定位
	public static String MAP_SDK_KEY = "seIhcSk2TiTqdHTjyGG8sjCn";// 个人
	// public static String MAP_AK = "Io8gL4Aybx3CZsdpSoKCqZPB";// 个人
	public static String MAP_AK = "nGf4WdZTlTgAjFSQ0AtuSWjc";// zmax
	
	public static String IP_LOCATION_URL = "http://api.map.baidu.com/location/ip";
	
	public static String CUR_CITY = "武汉";
	
	public static final String FEEDBACK_SENDED_ACTION = "feedback_sended_action";
	
	public static class Documents {
		
		public static final String DOCUMENTS_TYPE_KEY = "documents_type_key";
		public static final int PROTOCAL_TYPE = 1;
		public static final int GUIDE_TYPE = 2;
		public static final int RIGHT_TYPE = 3;
		
		public static final String PROTOCAL_SPF_KEY = "protocal_spf_key";
		public static final String GUIDE_SPF_KEY = "guide_spf_key";
		public static final String RIGHT_SPF_KEY = "right_spf_key";
		
	}
	
	public static class Acts {
		
		public static final String ID_KEY = "act_id_key";
		public static final String HOTEL_ID_KEY = "hotel_id_key";
		public static final String DATE_KEY = "act_date_key";
		public static final String CITY_KEY = "act_city_key";
		
	}
	
	public static final int PER_NUM_GET_ACTLIST = 5;
	public static final int PER_NUM_GET_HOTELLIST = 20;
	
	public static class SPFKEY {
		
		public static final String CITY_LOCATION_KEY = "city_location_key";
		public static final String INDEX_ACTLIST_KEY = "index_actlist_key";
		public static final String INDEX_ACTLIST_PAGENUM_KEY = "index_actlist_pagenum_key";
		
		public static final String INDEX_HOTELLIST_KEY = "index_hotellist_key";
		
	}
	
	public static List<Object> getFalseDataObject(boolean bo) {
		
		List<Object> mList = new ArrayList<Object>();
		
		for (int i = 0; i < 10; i++) {
			Act activityDetail = new Act();
			
			mList.add(activityDetail);
			
		}
		
		return mList;
	}
	
	public static List<Act> getFalseData(boolean bo) {
		
		List<Act> mList = new ArrayList<Act>();
		
		for (int i = 0; i < 30; i++) {
			Act activityDetail = new Act();
			
			mList.add(activityDetail);
			
		}
		
		return mList;
	}
	
	public static List<Act> getFalseData(int count) {
		
		List<Act> mList = new ArrayList<Act>();
		
		for (int i = 0; i < count; i++) {
			Act activityDetail = new Act();
			
			mList.add(activityDetail);
			
		}
		
		return mList;
	}
	
	public static List<View> getHotelFalseDataView(final FragmentActivity fragmentActivity, LayoutInflater inflater) {
		
		List<View> mList = new ArrayList<View>();
		
		for (int i = 0; i < 3; i++) {
			View view = inflater.inflate(R.layout.hotel_book_list_item, null);
			
			view.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					fragmentActivity.startActivity(new Intent(fragmentActivity, HotelDetailActivity.class));
					
				}
			});
			mList.add(view);
			
		}
		List<Map<String, String>> lists = new ArrayList<Map<String, String>>();
		for (int i = 0; i < 5; i++) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("date", "20121212");
			map.put("name", "GuangZhou op en   date ");
			lists.add(map);
		}
		
		mList.add(getHotelUpcomingFalseDataView(fragmentActivity, inflater, lists));
		
		return mList;
	}
	
	private static View getHotelUpcomingFalseDataView(final FragmentActivity fragmentActivity, LayoutInflater inflater,
			List<Map<String, String>> lists) {
		View view = inflater.inflate(R.layout.hotel_book_list_upcoming, null);
		ListView listView = (ListView) view.findViewById(R.id.list_view);
		SimpleAdapter simpleAdapter = new SimpleAdapter(fragmentActivity, lists, R.layout.hotel_book_upcoming_list_item, new String[] {
				"date", "name" }, new int[] { R.id.tv_date, R.id.tv_name });
		listView.setAdapter(simpleAdapter);
		return view;
		
	}
}
