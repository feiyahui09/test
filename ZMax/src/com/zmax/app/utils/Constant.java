package com.zmax.app.utils;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

import com.zmax.app.R;
import com.zmax.app.model.Act;
import com.zmax.app.ui.HotelDetailActivity;

public class Constant {
	public static final boolean LOGD_ENABLE = true;
	public static final boolean LOG_SDCARD_ENABLE = true; // 是否打印日志在SDcard上，正式发版前记得设置为false
	public static final String LOG_FILE_NAME = "com.zmax.app" + "_log.txt"; // 根据包名改变日志文件名字

	// 登陆成功后的SessionKEY
	public static String SESSION_KEY = "sessionKey";

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

	public static List<View> getHotelFalseDataView(final FragmentActivity fragmentActivity,
			LayoutInflater inflater) {

		List<View> mList = new ArrayList<View>();

		for (int i = 0; i < 10; i++) {
			View view = inflater.inflate(R.layout.hotel_book_list_item, null);
			((ImageView) view.findViewById(R.id.iv_img))
					.setBackgroundResource(R.drawable.icon2);

			((Button) view.findViewById(R.id.btn_book))
					.setOnClickListener(new OnClickListener() {
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
