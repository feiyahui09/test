package com.zmax.app.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;

import com.zmax.app.R;
import com.zmax.app.adapter.ActListAdapter;
import com.zmax.app.ui.base.BaseActivity;
import com.zmax.app.utils.Constant;
import com.zmax.app.widget.XListView;
import com.zmax.app.widget.XListView.IXListViewListener;

public class ActCalendarActivity extends BaseActivity implements IXListViewListener, OnItemClickListener {
	private Context mContext;
	private Button btn_Back;
	protected XListView listview;
	
	private ActListAdapter adapter;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_calender_list);
		mContext = this;
		initHeader();
		init();
	}
	
	private void init() {
		
		listview = (XListView) findViewById(R.id.list_view);
		listview.setPullLoadEnable(true);
		listview.setPullRefreshEnable(false);
		adapter = new ActListAdapter(this);
		adapter.appendToList(Constant.getFalseData(false));
		listview.setAdapter(adapter);
		listview.setOnItemClickListener(this);
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
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		Intent intent = new Intent();
		intent.setClass(mContext, ActDetailActivity.class);
		intent.putExtra(Constant.Acts.ID_KEY, 1);
		mContext.startActivity(intent);
		
	}
	
	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void onLoadMore() {
		// TODO Auto-generated method stub
		
	}
	
}
