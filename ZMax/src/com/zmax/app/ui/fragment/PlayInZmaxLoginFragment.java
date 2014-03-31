package com.zmax.app.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.zmax.app.R;
import com.zmax.app.ui.MainActivity;
import com.zmax.app.ui.fragment.PlayInZmaxFragment.PlayZmaxLogoutCallback;

public class PlayInZmaxLoginFragment extends Fragment {
	
	private View view;
	private Button btn_login;
	
	public PlayInZmaxLoginFragment() {
		this(R.color.white);
	}
	
	public PlayInZmaxLoginFragment(int colorRes) {
		setRetainInstance(true);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.playzmax_login, null);
		btn_login = (Button) view.findViewById(R.id.btn_login);
		btn_login.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				switchFragment(new PlayInZmaxFragment((PlayZmaxLogoutCallback) getActivity()));
			}
		});
		return view;
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}
	
	private void switchFragment(Fragment fragment) {
		if (getActivity() == null) return;
		
		if (getActivity() instanceof MainActivity) {
			MainActivity fca = (MainActivity) getActivity();
			fca.switchContent(fragment);
		}
		
	}
	
}
