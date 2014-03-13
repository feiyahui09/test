package com.zmax.app.utils;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.zmax.app.R;
import com.zmax.app.model.ActivityDetail;

public class Constant {
	public static final boolean LOGD_ENABLE = true;
	public static final boolean LOG_SDCARD_ENABLE = true; // 是否打印日志在SDcard上，正式发版前记得设置为false
	public static final String LOG_FILE_NAME = "com.zmax.app" + "_log.txt"; // 根据包名改变日志文件名字

	// 登陆成功后的SessionKEY
	public static String SESSION_KEY = "sessionKey";

	public static List<Object> getFalseDataObject(boolean bo) {

		List<Object> mList = new ArrayList<Object>();

		for (int i = 0; i < 10; i++) {
			ActivityDetail activityDetail = new ActivityDetail();
			if (bo)
				activityDetail.dt = "2";
			else
				activityDetail.dt = "12";
			mList.add(activityDetail);

		}

		return mList;
	}

	public static List<ActivityDetail> getFalseData(boolean bo) {

		List<ActivityDetail> mList = new ArrayList<ActivityDetail>();

		for (int i = 0; i < 10; i++) {
			ActivityDetail activityDetail = new ActivityDetail();
			if (bo)
				activityDetail.dt = "2";
			else
				activityDetail.dt = "12";
			mList.add(activityDetail);

		}

		return mList;
	}

	public static List<View> getFalseDataView(LayoutInflater inflater,
			boolean bo) {

		List<View> mList = new ArrayList<View>();

		for (int i = 0; i < 10; i++) {
			View view = inflater.inflate(R.layout.activities_list_item, null);
			((ImageView) view.findViewById(R.id.iv_img))
					.setBackgroundResource(R.drawable.ic_launcher);
			mList.add(view);

		}

		return mList;
	}

}
