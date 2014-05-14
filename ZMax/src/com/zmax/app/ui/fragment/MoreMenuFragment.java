package com.zmax.app.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.zmax.app.R;
import com.zmax.app.ui.MainActivity;
import com.zmax.app.ui.fragment.MoreMenuFragment.TabSelectedListener;
import com.zmax.app.utils.Constant;

public class MoreMenuFragment extends Fragment implements OnClickListener {
	private LinearLayout ll_menu_index, ll_menu_playzmax, ll_menu_myaccount, ll_menu_setting;
	
	public interface TabSelectedListener {
		
		public void handleSeleceted(int rid,boolean isInitial );
		
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
		
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.bedind_menu_list, null);
		
		ll_menu_index = (LinearLayout) v.findViewById(R.id.ll_menu_index);
		ll_menu_playzmax = (LinearLayout) v.findViewById(R.id.ll_menu_playzmax);
		ll_menu_myaccount = (LinearLayout) v.findViewById(R.id.ll_menu_myaccount);
		ll_menu_setting = (LinearLayout) v.findViewById(R.id.ll_menu_setting);
		ll_menu_index.setOnClickListener(this);
		ll_menu_playzmax.setOnClickListener(this);
		ll_menu_myaccount.setOnClickListener(this);
		ll_menu_setting.setOnClickListener(this);
		return v;
	}
	
	@Override
	public void onClick(View v) {
		Fragment newContent = null;
		switch (v.getId()) {
			case R.id.ll_menu_index:
				((TabSelectedListener) getActivity()).handleSeleceted(R.id.btn_activities,true);
				// newContent = new ActListFragment();
				break;
			case R.id.ll_menu_playzmax:
				
				if (Constant.getLogin() == null) {
					((TabSelectedListener) getActivity()).handleSeleceted(v.getId() ,true);
					// newContent = new PlayInZmaxLoginFragment();
				}
				else {
					((TabSelectedListener) getActivity()).handleSeleceted(v.getId()+100,true) ;
					// newContent = new PlayInZmaxFragment();
				}
				break;
			case R.id.ll_menu_myaccount:
				((TabSelectedListener) getActivity()).handleSeleceted(v.getId(),true);
				// newContent = new AccountFragment();
				break;
			case R.id.ll_menu_setting:
				((TabSelectedListener) getActivity()).handleSeleceted(v.getId(),true);
				// newContent = new SettingFragment();
				break;
		
		}
//		((SlidingFragmentActivity) getActivity()).getSlidingMenu().showContent();

//		  if (newContent != null) switchFragment(newContent);
		
	} // the meat of switching the above fragment
	
	private void switchFragment(Fragment fragment) {
		if (getActivity() == null) return;
		
		if (getActivity() instanceof MainActivity) {
			MainActivity fca = (MainActivity) getActivity();
			fca.switchContent(fragment);
		}
	}
	
}
