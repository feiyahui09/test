package com.zmax.app.ui;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.zmax.app.R;
import com.zmax.app.adapter.ActDetailAdapter;
import com.zmax.app.model.ActDetail;
import com.zmax.app.model.ActDetailContent;
import com.zmax.app.net.NetWorkHelper;
import com.zmax.app.task.GetActDetailTask;
import com.zmax.app.ui.base.BaseFragmentActivity;
import com.zmax.app.ui.fragment.ActDetailFirstFragment;
import com.zmax.app.ui.fragment.ActDetailSecondFragment;
import com.zmax.app.ui.fragment.ActDetailThirdFragment;
import com.zmax.app.utils.Constant;
import com.zmax.app.utils.ShareUtils;
import com.zmax.app.utils.StackBlurManager;
import com.zmax.app.utils.Utility;

import eu.inmite.android.lib.dialogs.ProgressDialogFragment;

public class ActDetailActivity extends BaseFragmentActivity {
	
	private Button btn_Back, btn_Share;
	
	private ViewPager pager;
	private ActDetailAdapter adapter;
	private ActDetailContent detailContent;
	private GetActDetailTask getActDetailTask;
	private String city, date;
	private LinearLayout ll_bg;
	private DialogFragment progressDialog;
	
	private int curPosition = 0;
	
	public interface RefreshDataCallBack {
		
		public void onDataRefresh(ActDetailContent detailContent);
		
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_detail);
		init();
		initHeader();
	}
	
	private void init() {
		city = getIntent().getStringExtra(Constant.Acts.CITY_KEY);
		date = getIntent().getStringExtra(Constant.Acts.DATE_KEY);
		
		ll_bg = (LinearLayout) findViewById(R.id.ll_bg);
		pager = (ViewPager) findViewById(R.id.pager);
		adapter = new ActDetailAdapter(this);
		pager.setAdapter(adapter);
		
		adapter.addTab(new ActDetailFirstFragment(city, date));
		adapter.addTab(new ActDetailSecondFragment());
		adapter.addTab(new ActDetailThirdFragment());
		
		pager.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int position) {
				curPosition = position;
				Fragment fragment = adapter.getItem(position);
				if (fragment instanceof RefreshDataCallBack) {
					((RefreshDataCallBack) fragment).onDataRefresh(detailContent);
					
				}
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
		if (!NetWorkHelper.checkNetState(this)) {
			Utility.toastNetworkFailed(this);
			return;
		}
		progressDialog = ProgressDialogFragment.createBuilder(this, getSupportFragmentManager()).setMessage("正在加载中...").setTitle("提示")
				.setCancelable(true).show();
		getActDetailTask = new GetActDetailTask(this, new GetActDetailTask.TaskCallBack() {
			
			@Override
			public void onCallBack(ActDetail result) {
				if (progressDialog != null && progressDialog.getActivity() != null) progressDialog.dismiss();
				
				if (result != null && result.status == 200 && result.event != null) {
					detailContent = result.event;
					Handler handler = new Handler();
					// 延迟刷新界面，确保fragment刷新在activitycreated后
					handler.postDelayed(new Runnable() {
						
						@Override
						public void run() {
							// initData(detailContent);
						}
					}, 2000);
					
					ImageLoader.getInstance().loadImage(detailContent.poster, new ImageLoadingListener() {
						
						@Override
						public void onLoadingStarted(String imageUri, View view) {
							// TODO Auto-generated method stub
							
						}
						
						@Override
						public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
							// TODO Auto-generated method stub
							
						}
						
						@Override
						public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
							try {
								ll_bg.setBackground(new BitmapDrawable(StackBlurManager.fastblur(ActDetailActivity.this, loadedImage, 11)));
							}
							catch (Exception e) {
								e.printStackTrace();
							}
							catch (Error e) {
								e.printStackTrace();
							}
							
						}
						
						@Override
						public void onLoadingCancelled(String imageUri, View view) {
							// TODO Auto-generated method stub
							
						}
					});
				}
				else
					Utility.toastFailedResult(ActDetailActivity.this);
				
			}
		});
		
		int actid = getIntent().getIntExtra(Constant.Acts.ID_KEY, -1);
		if (actid > 0) getActDetailTask.execute(String.valueOf(actid));
	}
	
	private void initData(ActDetailContent result) {
		// pager.setCurrentItem(curPosition);
		
		for (int i = 0; i < adapter.getCount(); i++) {
			Fragment fragment = adapter.getItem(i);
			if (fragment instanceof RefreshDataCallBack) {
				((RefreshDataCallBack) fragment).onDataRefresh(detailContent);
			}
		}
	}
	
	private void initHeader() {
		btn_Back = (Button) findViewById(R.id.btn_more);
		btn_Back.setBackgroundResource(R.drawable.top_back_sel);
		btn_Share = (Button) findViewById(R.id.btn_share);
		btn_Share.setVisibility(View.VISIBLE);
		btn_Share.setBackgroundResource(R.drawable.top_share_sel);
		btn_Back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		btn_Share.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				new ShareUtils().showShare(ActDetailActivity.this, false, null);
				
			}
		});
		
	}
	
}
