package com.zmax.app.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.zmax.app.R;
import com.zmax.app.ui.MainActivity;
import com.zmax.app.utils.Constant;
import com.zmax.app.utils.Utility;

public class MoreMenuFragment extends Fragment implements OnClickListener {
	private LinearLayout ll_menu_index, ll_menu_playzmax, ll_menu_myaccount, ll_menu_setting;
	private ImageView iv_menu_index, iv_menu_playzmax, iv_menu_myaccount, iv_menu_setting;
	private TextView tv_menu_index, tv_menu_playzmax, tv_menu_myaccount, tv_menu_setting;

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

		iv_menu_index = (ImageView) v.findViewById(R.id.iv_menu_index);
		iv_menu_playzmax = (ImageView) v.findViewById(R.id.iv_menu_playzmax);
		iv_menu_myaccount = (ImageView) v.findViewById(R.id.iv_menu_myaccount);
		iv_menu_setting = (ImageView) v.findViewById(R.id.iv_menu_setting);
		if(Utility.isVersionOk())
		iv_menu_index.setBackground(getResources().getDrawable(R.drawable.menu_index_actived));

		tv_menu_index = (TextView) v.findViewById(R.id.tv_menu_index);
		tv_menu_playzmax = (TextView) v.findViewById(R.id.tv_menu_playzmax);
		tv_menu_myaccount = (TextView) v.findViewById(R.id.tv_menu_myaccount);
		tv_menu_setting = (TextView) v.findViewById(R.id.tv_menu_setting);
		tv_menu_index.setTextColor(getResources().getColor(R.color.default_menu_actived));

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
				((TabSelectedListener) getActivity()).handleSeleceted(R.id.btn_activities, true);
				if (Utility.isVersionOk()){
					unCheckAll();
					iv_menu_index.setBackground(getResources().getDrawable(R.drawable.menu_index_actived));
					tv_menu_index.setTextColor(getResources().getColor(R.color.default_menu_actived));
				}

				break;
			case R.id.ll_menu_playzmax:

				if (Constant.getLogin() == null){
					((TabSelectedListener) getActivity()).handleSeleceted(v.getId(), true);
					// newContent = new PlayInZmaxLoginFragment();
				} else {
					((TabSelectedListener) getActivity()).handleSeleceted(v.getId() + 100, true);
					// newContent = new PlayInZmaxFragment();
				}

				if (Utility.isVersionOk()){
					unCheckAll();
					iv_menu_playzmax.setBackground(getResources().getDrawable(R.drawable.menu_playzmax_actived));
					tv_menu_playzmax.setTextColor(getResources().getColor(R.color.default_menu_actived));

				}
				break;
			case R.id.ll_menu_myaccount:
				((TabSelectedListener) getActivity()).handleSeleceted(v.getId(), true);
				if (Utility.isVersionOk()){
					unCheckAll();
					iv_menu_myaccount.setBackground(getResources().getDrawable(R.drawable.menu_myaccount_actived));
					tv_menu_myaccount.setTextColor(getResources().getColor(R.color.default_menu_actived));

				}
				break;
			case R.id.ll_menu_setting:
				((TabSelectedListener) getActivity()).handleSeleceted(v.getId(), true);
				if (Utility.isVersionOk()){
					unCheckAll();
					iv_menu_setting.setBackground(getResources().getDrawable(R.drawable.menu_setting_actived));
					tv_menu_setting.setTextColor(getResources().getColor(R.color.default_menu_actived));

				}
				break;

		}
//		((SlidingFragmentActivity) getActivity()).getSlidingMenu().showContent();

//		  if (newContent != null) switchFragment(newContent);

	} // the meat of switching the above fragment

	private void unCheckAll() {
		iv_menu_index.setBackground(getResources().getDrawable(R.drawable.menu_index_normal));
		iv_menu_playzmax.setBackground(getResources().getDrawable(R.drawable.menu_playzmax_normal));
		iv_menu_myaccount.setBackground(getResources().getDrawable(R.drawable.menu_myaccount_normal));
		iv_menu_setting.setBackground(getResources().getDrawable(R.drawable.menu_setting_normal));


		tv_menu_index.setTextColor(getResources().getColor(R.color.default_menu_normal));
		tv_menu_playzmax.setTextColor(getResources().getColor(R.color.default_menu_normal));
		tv_menu_myaccount.setTextColor(getResources().getColor(R.color.default_menu_normal));
		tv_menu_setting.setTextColor(getResources().getColor(R.color.default_menu_normal));

	}


	@Deprecated
	private void switchFragment(Fragment fragment) {
		if (getActivity() == null) return;

		if (getActivity() instanceof MainActivity){
			MainActivity fca = (MainActivity) getActivity();
			fca.switchContent(fragment);
		}
	}

	public interface TabSelectedListener {

		public void handleSeleceted(int rid, boolean isInitial);

	}

}
