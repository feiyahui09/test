package com.zmax.app.ui;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;

import com.zmax.app.R;
import com.zmax.app.adapter.ActListAdapter;
import com.zmax.app.model.Act;
import com.zmax.app.model.ActList;
import com.zmax.app.net.NetWorkHelper;
import com.zmax.app.task.GetActListInHotelTask;
import com.zmax.app.task.GetActListTask;
import com.zmax.app.ui.base.BaseActivity;
import com.zmax.app.utils.Constant;
import com.zmax.app.utils.Utility;
import com.zmax.app.widget.XListView;
import com.zmax.app.widget.XListView.IXListViewListener;

public class ActsInHotelActivity extends BaseActivity implements IXListViewListener, OnItemClickListener {
	private Context mContext;
	private Button btn_Back;
	protected XListView listview;
	private TextView tv_title;
	
	private ActListAdapter adapter;
	private GetActListInHotelTask getActListTask;
	private int curPage = 1;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_list_in_hotel);
		mContext = this;
		init();
		onRefresh();
	}
	
	private void init() {
		tv_title = (TextView) findViewById(R.id.tv_title);
		tv_title.setText("酒店附近的活动");
		btn_Back = (Button) findViewById(R.id.btn_back);
		btn_Back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		listview = (XListView) findViewById(R.id.list_view);
		listview.setPullLoadEnable(true);
		listview.setPullRefreshEnable(true);
		adapter = new ActListAdapter(this);
		listview.setAdapter(adapter);
		listview.setOnItemClickListener(this);
		listview.setXListViewListener(this);
		curPage = 1;
		
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		try {
			Act act = (Act) adapter.getItem(position - 1);
			Intent intent = new Intent();
			intent.setClass(this, ActDetailActivity.class);
			intent.putExtra(Constant.Acts.ID_KEY, act.id);
			intent.putExtra(Constant.Acts.CITY_KEY, act.cities);
			intent.putExtra(Constant.Acts.DATE_KEY, act.duration);
			startActivity(intent);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void getActList(int page) {
		if (!NetWorkHelper.checkNetState(this)) {
			Utility.toastNetworkFailed(mContext);
			return;
		}
		
		getActListTask = new GetActListInHotelTask(this, new GetActListInHotelTask.TaskCallBack() {
			
			@Override
			public void onCallBack(ActList result) {
				if (result != null && result.status == 200) {
					List<Act> actList = result.events;
					if (actList != null && !actList.isEmpty()) {
						// if (curPage == 1) adapter.Clear();
						adapter.appendToList(actList);
						curPage++;
						listview.onLoad();
					}
					else if (actList != null && actList.isEmpty()) {
						Utility.toastNoMoreResult(mContext);
						listview.onLoads();
					}
				}
				else
					Utility.toastFailedResult(mContext);
			}
		});
		String hotelId = getIntent().getStringExtra(Constant.Acts.HOTEL_ID_KEY);
		if (!TextUtils.isEmpty(hotelId)) getActListTask.execute(String.valueOf(hotelId), String.valueOf(page), "5");
	}
	
	@Override
	public void onRefresh() {
		curPage = 1;
		getActList(curPage);
		listview.onLoad();
	}
	
	@Override
	public void onLoadMore() {
		getActList(curPage);
		
	}
	
}
