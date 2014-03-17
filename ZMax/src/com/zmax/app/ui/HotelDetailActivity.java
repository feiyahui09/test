package com.zmax.app.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.zmax.app.R;
import com.zmax.app.map.RoutePlanDemo;
import com.zmax.app.ui.base.BaseActivity;
import com.zmax.app.utils.Utility;

public class HotelDetailActivity extends BaseActivity implements
		OnClickListener {
	private Context mContext;

	private Button btn_Back, btn_Share;
	private LinearLayout ll_act_calendar, ll_address, ll_phone,
	ll_hotel_facility, ll_date_pick, ll_room_list, ll_comment;
	private ImageView iv_head_img;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.hotel_detail);
		init();
		initHeader();
	}

	private void init() {
		mContext = this;
		iv_head_img = (ImageView) findViewById(R.id.iv_head_img);
		ll_act_calendar = (LinearLayout) findViewById(R.id.ll_act_calendar);
		ll_address = (LinearLayout) findViewById(R.id.ll_address);
		ll_phone = (LinearLayout) findViewById(R.id.ll_phone);
		ll_hotel_facility = (LinearLayout) findViewById(R.id.ll_hotel_facility);
		ll_date_pick = (LinearLayout) findViewById(R.id.ll_date_pick);
		ll_room_list = (LinearLayout) findViewById(R.id.ll_room_list);
		ll_comment = (LinearLayout) findViewById(R.id.ll_comment);

		iv_head_img.setOnClickListener(this);
		ll_act_calendar.setOnClickListener(this);
		ll_address.setOnClickListener(this);
		ll_phone.setOnClickListener(this);
		ll_hotel_facility.setOnClickListener(this);
		ll_date_pick.setOnClickListener(this);
		ll_room_list.setOnClickListener(this);
		ll_comment.setOnClickListener(this);
	}

	private void initHeader() {
		btn_Back = (Button) findViewById(R.id.btn_back);
		btn_Share = (Button) findViewById(R.id.btn_share);

		btn_Back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		btn_Share.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

			}
		});
	}

	@Override
	public void onClick(View v) {
		Intent intent = new Intent();

		switch (v.getId()) {
		case R.id.iv_head_img:

			break;
		case R.id.ll_act_calendar:
			intent.setClass(mContext, ActCalendarActivity.class);
			mContext.startActivity(intent);
			break;

		case R.id.ll_address:
			intent.setClass(mContext, RoutePlanDemo.class);
			mContext.startActivity(intent);

			break;
		case R.id.ll_phone:
			Utility.goDialPhone(mContext, "10086");
			break;
		case R.id.ll_hotel_facility:
			intent.setClass(mContext, HotelFacilityActivity.class);
			mContext.startActivity(intent);
			break;
		case R.id.ll_date_pick:
			intent.setClass(mContext, HotelDatePickActivity.class);
			mContext.startActivity(intent);
			break;
		case R.id.ll_room_list:
			intent.setClass(mContext, OrderFillActivity.class);
			mContext.startActivity(intent);
			break;
		case R.id.ll_comment:
			intent.setClass(mContext, HotelCommentActivity.class);
			mContext.startActivity(intent);
			break;

		default:
			break;
		}
	}

}
