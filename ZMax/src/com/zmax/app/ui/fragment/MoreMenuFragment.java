package com.zmax.app.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.zmax.app.R;
import com.zmax.app.ui.MainActivity;

public class MoreMenuFragment extends Fragment implements OnClickListener {
	private LinearLayout ll_menu_index, ll_menu_playzmax, ll_menu_myaccount,
			ll_menu_setting;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
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
			newContent = new ActListFragment(R.color.red);
			break;
		case R.id.ll_menu_playzmax:
			newContent = new PlayInZmaxFragment(R.color.green);
			break;
		case R.id.ll_menu_myaccount:
			newContent = new AccountFragment(R.color.blue);
			break;
		case R.id.ll_menu_setting:
			newContent = new SettingFragment(android.R.color.white);
			break;

		}
		if (newContent != null)
			switchFragment(newContent);

	} // the meat of switching the above fragment

	private void switchFragment(Fragment fragment) {
		if (getActivity() == null)
			return;

		if (getActivity() instanceof MainActivity) {
			MainActivity fca = (MainActivity) getActivity();
			fca.switchContent(fragment);
		}
	}

}
