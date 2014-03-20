package com.zmax.app.ui;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.zmax.app.R;
import com.zmax.app.adapter.ActDetailAdapter;
import com.zmax.app.ui.base.BaseFragmentActivity;
import com.zmax.app.ui.fragment.ActDetailFirstFragment;
import com.zmax.app.ui.fragment.ActDetailSecondFragment;
import com.zmax.app.ui.fragment.ActDetailThirdFragment;
import com.zmax.app.ui.fragment.RoomControlAirConditionFragment;

public class RoomControlActivity extends BaseFragmentActivity {
	private static final String TAG = RoomControlActivity.class.getSimpleName();

	private Button btn_Back;
	private TextView tv_title;
	private ViewPager pager;
	private ActDetailAdapter adapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.room_control);
		initHeader();
		init();
		initData();
	}

	private void init() {

		pager = (ViewPager) findViewById(R.id.pager);

		adapter = new ActDetailAdapter(this);
		pager.setAdapter(adapter);

	}

	private void initData() {

		adapter.addTab(new RoomControlAirConditionFragment(R.color.red));
		adapter.addTab(new RoomControlAirConditionFragment());
		adapter.addTab(new RoomControlAirConditionFragment(R.color.white));
	}

	private void initHeader() {
		btn_Back = (Button) findViewById(R.id.btn_back);
		btn_Back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		tv_title = (TextView) findViewById(R.id.tv_title);
		tv_title.setText(getString(R.string.room_control));
	}

}
