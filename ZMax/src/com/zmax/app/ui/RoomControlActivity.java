package com.zmax.app.ui;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.zmax.app.R;
import com.zmax.app.adapter.ActDetailAdapter;
import com.zmax.app.model.RoomStatus;
import com.zmax.app.net.NetWorkHelper;
import com.zmax.app.task.GetRoomStatusTask;
import com.zmax.app.ui.base.BaseFragmentActivity;
import com.zmax.app.ui.fragment.RoomControlAirConditionFragment;
import com.zmax.app.ui.fragment.RoomControlLightingFragment;
import com.zmax.app.ui.fragment.RoomControlTVFragment;
import com.zmax.app.widget.SmartViewPager;

public class RoomControlActivity extends BaseFragmentActivity {
	
	public interface VerticalChangedCallback {
		public void onCallBack(boolean isCurAbove);
	}
	
	public interface PageChangedCallback {
		public void onPageChanegdCallBack(int index);
	}
	
	private static final String TAG = RoomControlActivity.class.getSimpleName();
	
	private ViewGroup above_content_header;
	private Button btn_Back;
	private TextView tv_title;
	private SmartViewPager pager;
	private ActDetailAdapter adapter;
	private Context mContext;
	private VerticalChangedCallback callback;
	private PageChangedCallback pageChangedCallback;
	public static boolean isCurAbove = true;
	private ImageView iv_right, iv_left;
	private int curPageIndex = 0;
	
	private GetRoomStatusTask getRoomStatusTask;
	private ProgressDialog progressDialog;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.room_control);
		initHeader();
		init();
		progressDialog = new ProgressDialog(this);
		progressDialog.setTitle("提示");
		progressDialog.setCancelable(false);
		progressDialog.setMessage("正在更新房间控制信息中！");
		progressDialog.show();
		getRoomStatusTask = new GetRoomStatusTask(this, new GetRoomStatusTask.TaskCallBack() {
			@Override
			public void onCallBack(RoomStatus result) {
				if (progressDialog != null && progressDialog.isShowing()) progressDialog.cancel();
				if (result == null) {
					if (!NetWorkHelper.checkNetState(mContext))
						Toast.makeText(mContext, mContext.getString(R.string.httpProblem), 450).show();
					else
						Toast.makeText(mContext, mContext.getString(R.string.unkownError), 450).show();
				}
				else if (result.status != 200)
					Toast.makeText(mContext, "" + result.message, 450).show();
				else {
					initData(result);
				}
				
			}
		});
		getRoomStatusTask.execute();
	}
	
	private void init() {
		mContext = this;
		iv_right = (ImageView) findViewById(R.id.iv_right);
		iv_left = (ImageView) findViewById(R.id.iv_left);
		iv_right.setVisibility(View.VISIBLE);
		iv_left.setVisibility(View.VISIBLE);
		iv_left.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (curPageIndex > 0) pager.setCurrentItem(--curPageIndex);
			}
		});
		iv_right.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (curPageIndex < adapter.getCount() - 1) pager.setCurrentItem(++curPageIndex);
			}
		});
		
		pager = (SmartViewPager) findViewById(R.id.pager);
		adapter = new ActDetailAdapter(this);
		pager.setAdapter(adapter);
		// retained buffer size 6
		pager.setOffscreenPageLimit(3);
		pager.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int position) {
				curPageIndex = position;
				showIndicator();
			}
			
			@Override
			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onPageScrollStateChanged(int state) {
				// TODO Auto-generated method stub
				
			}
		});
		callback = new VerticalChangedCallback() {
			
			@Override
			public void onCallBack(boolean isCurAbove) {
				if (isCurAbove) {
					
					showIndicator();
					pager.setCanScroll(true);
				}
				else {
					hideIndicator();
					pager.setCanScroll(false);
				}
				
			}
		};
		pageChangedCallback = new PageChangedCallback() {
			
			@Override
			public void onPageChanegdCallBack(int index) {
				
				pager.setCurrentItem(index);
				
			}
		};
	}
	
	private void initData(RoomStatus result) {
		
		adapter.addTab(new RoomControlLightingFragment(callback, result.light));
		adapter.addTab(new RoomControlAirConditionFragment(callback, result.airCondition));
		adapter.addTab(new RoomControlTVFragment(callback, result.television));
		// adapter.addTab(new RoomControlCurtainFragment(callback));
		// adapter.addTab(new RoomControlWakeUpFragment(callback));
		adapter.notifyDataSetChanged();
		pager.setCurrentItem(0);
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
	
	private void hideIndicator() {
		iv_right.setVisibility(View.GONE);
		iv_left.setVisibility(View.GONE);
	}
	
	private void showIndicator() {
		
		if (curPageIndex == 0) {
			iv_right.setVisibility(View.VISIBLE);
			iv_left.setVisibility(View.GONE);
		}
		else if (curPageIndex == adapter.getCount() - 1) {
			
			iv_right.setVisibility(View.GONE);
			iv_left.setVisibility(View.VISIBLE);
		}
		else {
			iv_right.setVisibility(View.VISIBLE);
			iv_left.setVisibility(View.VISIBLE);
		}
		
	}
	
}
