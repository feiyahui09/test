package com.zmax.app.ui;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.zmax.app.R;
import com.zmax.app.adapter.ActDetailAdapter;
import com.zmax.app.ui.RoomControlActivity.VerticalChangedCallback;
import com.zmax.app.ui.base.BaseFragmentActivity;
import com.zmax.app.ui.fragment.RoomControlAirConditionFragment;
import com.zmax.app.ui.fragment.RoomControlLightingFragment;
import com.zmax.app.ui.fragment.RoomControlTVFragment;
import com.zmax.app.widget.SmartViewPager;

public class RoomControlActivity extends BaseFragmentActivity {

	public interface VerticalChangedCallback {
		public void onCallBack(boolean isCurAbove);
	}

	private static final String TAG = RoomControlActivity.class.getSimpleName();

	private ViewGroup above_content_header;
	private Button btn_Back;
	private TextView tv_title;
	private SmartViewPager pager;
	private ActDetailAdapter adapter;
	private VerticalChangedCallback callback;

	public static boolean isCurAbove = true;

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

		pager = (SmartViewPager) findViewById(R.id.pager);
		adapter = new ActDetailAdapter(this);
		pager.setAdapter(adapter);
		pager.setOffscreenPageLimit(3);
		callback = new VerticalChangedCallback() {

			@Override
			public void onCallBack(boolean isCurAbove) {
				if (isCurAbove) {
					// above_content_header.setVisibility(View.VISIBLE);
					pager.setCanScroll(true);
				} else {
					// above_content_header.setVisibility(View.GONE);
					pager.setCanScroll(false);
				}

			}
		};

	}

	private void initData() {
		adapter.addTab(new RoomControlLightingFragment(callback));
		 adapter.addTab(new RoomControlTVFragment(callback));
		 adapter.addTab(new RoomControlAirConditionFragment(callback));
	

	}

	private void initHeader() {
		above_content_header = (ViewGroup) findViewById(R.id.above_content_header);
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
