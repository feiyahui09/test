package com.zmax.app.ui;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.zmax.app.R;
import com.zmax.app.adapter.HotelFacilityListAdapter;
import com.zmax.app.ui.base.BaseActivity;
import com.zmax.app.utils.Constant;
import com.zmax.app.widget.XListView;

public class HotelFacilityActivity extends BaseActivity    {
	private Context mContext;

	private Button btn_Back;
	private TextView tv_title;
	private XListView listView;
	private HotelFacilityListAdapter adapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.hotel_facility_list);
		init();
		initHeader();
	}

	private void init() {
		mContext = this;
		tv_title = (TextView) findViewById(R.id.tv_title);
		tv_title.setText(getResources().getString(R.string.hotel_facility));
		listView=(XListView)findViewById(R.id.list_view);
		listView.setPullLoadEnable(false);
		listView.setPullRefreshEnable(false);
		adapter = new HotelFacilityListAdapter( mContext);
		adapter.appendToList(Constant.getFalseData(false));
		listView.setAdapter(adapter);

	}

	private void initHeader() {
		btn_Back = (Button) findViewById(R.id.btn_back);

		btn_Back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

}
