package com.zmax.app.ui;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zmax.app.R;
import com.zmax.app.adapter.HotelCommentListAdapter;
import com.zmax.app.adapter.HotelFacilityListAdapter;
import com.zmax.app.ui.base.BaseActivity;
import com.zmax.app.utils.Constant;
import com.zmax.app.widget.XListView;

public class HotelCommentActivity extends BaseActivity    {
	private Context mContext;

	private Button btn_Back;
	private TextView tv_title;
	private XListView listView;
	private TextView tv_bottom_more;
	private HotelCommentListAdapter adapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.hotel_comment_list);
		init();
		initHeader();
		
	}

	private void init() {
		mContext = this;
		tv_title = (TextView) findViewById(R.id.tv_title);
		tv_title.setText(getResources().getString(R.string.hotel_comment));
		tv_bottom_more =(TextView)findViewById(R.id.tv_bottom_more);
		tv_bottom_more.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}
		});
		listView=(XListView)findViewById(R.id.list_view);
		listView.setPullLoadEnable(false);
		listView.setPullRefreshEnable(true);
		adapter = new HotelCommentListAdapter( mContext);
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
