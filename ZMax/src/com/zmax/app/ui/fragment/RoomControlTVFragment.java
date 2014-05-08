package com.zmax.app.ui.fragment;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.zmax.app.R;
import com.zmax.app.adapter.RoomControlAdapter;
import com.zmax.app.model.Television;
import com.zmax.app.net.NetWorkHelper;
import com.zmax.app.task.SetTelevisionTask;
import com.zmax.app.ui.RoomControlActivity.PageChangedCallback;
import com.zmax.app.ui.RoomControlActivity.VerticalChangedCallback;
import com.zmax.app.utils.Utility;
import com.zmax.app.widget.VerticalViewPager;

public class RoomControlTVFragment extends Fragment {
	
	protected View view;
	private VerticalViewPager vpager;
	private RoomControlAdapter adapter;
	private VerticalChangedCallback callback;
	private PageChangedCallback pageChangedCallback;
	private SetTelevisionTask task;
	
	private Television television;
	private boolean isEnable;
	
	/**
	 * behind views
	 */
	public RoomControlTVFragment(VerticalChangedCallback callback) {
		this.callback = callback;
		setRetainInstance(true);
	}
	
	public RoomControlTVFragment(VerticalChangedCallback callback, Television television) {
		this.callback = callback;
		this.television = television;
		setRetainInstance(true);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.room_control_fragment, null);
		vpager = (VerticalViewPager) view.findViewById(R.id.vpager);
		adapter = new RoomControlAdapter(getActivity(), null);
		vpager.setAdapter(adapter);
		adapter.addViews(getTVView(getActivity(), inflater));
		vpager.setOnPageChangeListener(new VerticalViewPager.OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int position) {
				if (callback != null) {
					callback.onCallBack(position == 0 ? true : false);
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
		return view;
	}
	
	private void initData() {
		
		if (television == null) return;
		isEnable = television.status == 1 ? true : false;
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}
	
	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		if (isVisibleToUser) {
			// 相当于Fragment的onResume
			if (callback != null) {
				if (adapter != null && adapter.getCount() > 0)
					callback.onCallBack(vpager.getCurrentItem() == 0 ? true : false);
				else
					callback.onCallBack(true);
				
			}
		}
	}
	
	public List<View> getTVView(final FragmentActivity fragmentActivity, LayoutInflater inflater) {
		
		List<View> mList = new ArrayList<View>();
		mList.add(getAbove(inflater));
		mList.add(getTVBehind(inflater));
		
		return mList;
	}
	
	private View getAbove(LayoutInflater inflater) {
		final View view = inflater.inflate(R.layout.room_control_above, null);
		ImageView big_icon = ((ImageView) view.findViewById(R.id.iv_big_logo));
		big_icon.setImageResource(R.drawable.room_control_above_tv);
		TextView tv_mode = (TextView) view.findViewById(R.id.tv_mode_tile);
		tv_mode.setText("电视");
		TextView tv_mode_detail = ((TextView) view.findViewById(R.id.tv_mode_detail));
		tv_mode_detail.setVisibility(View.GONE);
		
		return view;
	}
	
	private View getTVBehind(LayoutInflater inflater) {
		final LinearLayout ll_digital, ll_orient;
		RadioGroup rg_model;
		RadioButton rb_orient, rb_digital;
		final View view = inflater.inflate(R.layout.room_control_tv_behind, null);
		View ib_on, btn_at, btn_volu, btn_chd, btn_vold, btn_chu, btn_no_0, btn_no_1, btn_no_2, btn_no_3, btn_no_4, btn_no_5, btn_no_6, btn_no_7, btn_no_8, btn_no_9;
		CheckBox cb_sil;
		
		cb_sil = (CheckBox) view.findViewById(R.id.cb_sil);
		ll_digital = (LinearLayout) view.findViewById(R.id.ll_digital);
		ll_orient = (LinearLayout) view.findViewById(R.id.ll_orient);
		rg_model = ((RadioGroup) view.findViewById(R.id.rg_model));
		rb_orient = ((RadioButton) view.findViewById(R.id.rb_orient));
		rb_digital = ((RadioButton) view.findViewById(R.id.rb_digital));
		
		ib_on = view.findViewById(R.id.ib_on);
		btn_at = view.findViewById(R.id.btn_at);
		btn_volu = view.findViewById(R.id.btn_volu);
		btn_chd = view.findViewById(R.id.btn_chd);
		btn_vold = view.findViewById(R.id.btn_vold);
		btn_chu = view.findViewById(R.id.btn_chu);
		btn_no_0 = view.findViewById(R.id.btn_no_0);
		btn_no_1 = view.findViewById(R.id.btn_no_1);
		btn_no_2 = view.findViewById(R.id.btn_no_2);
		btn_no_3 = view.findViewById(R.id.btn_no_3);
		btn_no_4 = view.findViewById(R.id.btn_no_4);
		btn_no_5 = view.findViewById(R.id.btn_no_5);
		btn_no_6 = view.findViewById(R.id.btn_no_6);
		btn_no_7 = view.findViewById(R.id.btn_no_7);
		btn_no_8 = view.findViewById(R.id.btn_no_8);
		btn_no_9 = view.findViewById(R.id.btn_no_9);
		
		ib_on.setOnClickListener(onClickListener);
		btn_at.setOnClickListener(onClickListener);
		btn_volu.setOnClickListener(onClickListener);
		btn_chd.setOnClickListener(onClickListener);
		btn_vold.setOnClickListener(onClickListener);
		btn_chu.setOnClickListener(onClickListener);
		btn_no_0.setOnClickListener(onClickListener);
		btn_no_1.setOnClickListener(onClickListener);
		btn_no_2.setOnClickListener(onClickListener);
		btn_no_3.setOnClickListener(onClickListener);
		btn_no_4.setOnClickListener(onClickListener);
		btn_no_5.setOnClickListener(onClickListener);
		btn_no_6.setOnClickListener(onClickListener);
		btn_no_7.setOnClickListener(onClickListener);
		btn_no_8.setOnClickListener(onClickListener);
		btn_no_9.setOnClickListener(onClickListener);
		cb_sil.setOnClickListener(onClickListener);
		
		rg_model.setOnCheckedChangeListener(new android.widget.RadioGroup.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				if (checkedId == R.id.rb_digital) {
					ll_digital.setVisibility(View.VISIBLE);
					ll_orient.setVisibility(View.GONE);
				}
				else if (checkedId == R.id.rb_orient) {
					ll_digital.setVisibility(View.GONE);
					ll_orient.setVisibility(View.VISIBLE);
				}
			}
		});
		rb_orient.setChecked(true);
		initData();
		return view;
	}
	
	OnClickListener onClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
				case R.id.ib_on:
					set("on");
					break;
				case R.id.btn_at:
					set("at");
					break;
				case R.id.btn_volu:
					set("volu");
					break;
				case R.id.btn_vold:
					set("vold");
					break;
				case R.id.btn_chd:
					set("chd");
					break;
				case R.id.btn_chu:
					set("chu");
					break;
				case R.id.btn_no_0:
					set("0");
					break;
				case R.id.btn_no_1:
					set("1");
					break;
				case R.id.btn_no_2:
					set("2");
					break;
				case R.id.btn_no_3:
					set("3");
					break;
				case R.id.btn_no_4:
					set("4");
					break;
				case R.id.btn_no_5:
					set("5");
					break;
				case R.id.btn_no_6:
					set("6");
					break;
				case R.id.btn_no_7:
					set("7");
					break;
				case R.id.btn_no_8:
					set("8");
					break;
				case R.id.btn_no_9:
					set("9");
					break;
				case R.id.cb_sil:
					set("sil");
					break;
				default:
					break;
			}
		}
	};
	
	private void set(String push_button) {
		if (!isEnable) return;
		task = new SetTelevisionTask(getActivity(), new SetTelevisionTask.TaskCallBack() {
			@Override
			public void onCallBack(Television result) {
				
				if (getActivity() == null) {
					return;
				}
				if (result == null) {
					if (!NetWorkHelper.checkNetState(getActivity()))
						Toast.makeText(getActivity(), getActivity().getString(R.string.httpProblem), 450).show();
					else
						Utility.toastResult(getActivity(), getActivity().getString(R.string.unkownError));
				}
				else if (result.status == 200) {
					
				}
				else if (result.status == 401) {
					
					Utility.showTokenErrorDialog(getActivity(), result.message);
				}
				else {
					Utility.toastResult(getActivity(), result.message);
				}
				// if(result.intValue()!=200)
			}
		});
		task.execute(push_button);
	}
	
}
